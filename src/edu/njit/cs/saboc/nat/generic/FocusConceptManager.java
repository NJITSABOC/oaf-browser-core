package edu.njit.cs.saboc.nat.generic;

import edu.njit.cs.saboc.nat.generic.history.FocusConceptHistory;
import edu.njit.cs.saboc.blu.core.ontology.Concept;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Class for managing the current focus concept in the NAT. 
 * Handles navigation between different concepts and 
 * calling listeners to inform them that the focus concept
 * changed.
 * 
 * @author Chris O
 * @param <T> 
 */
public class FocusConceptManager<T extends Concept> {
    
    /**
     * Listener for handling the focus concept changing
     * 
     * @param <T> 
     */
    public interface FocusConceptListener<T extends Concept> {
        public void focusConceptChanged(T concept);
        public void reloadFocusConcept();
    }
    
    private final NATBrowserPanel<T> mainPanel;
    
    private final ArrayList<FocusConceptListener<T>> listeners = new ArrayList<>();

    private final FocusConceptHistory<T> history = new FocusConceptHistory<>();
    
    private Optional<T> activeFocusConcept = Optional.empty();
   
    public FocusConceptManager(NATBrowserPanel<T> mainPanel) {
        this.mainPanel = mainPanel;
    }

    public FocusConceptHistory<T> getHistory() {
        return history;
    }
    
    public void addFocusConceptListener(FocusConceptListener<T> fcl) {
        listeners.add(fcl);
    }
    
    public void removeFocusConceptListener(FocusConceptListener<T> fcl) {
        listeners.remove(fcl);
    }

    public void navigateToRoot() {
        
        if(mainPanel.getDataSource().isPresent()) {
            navigateTo(mainPanel.getDataSource().get().getOntology().getConceptHierarchy().getRoot());
        }
    }
    
    /**
     * Reloads all panels for the current focus concept
     */
    public final void refresh() {

        if (!mainPanel.getDataSource().isPresent()) {
            return;
        }

        listeners.forEach((listener) -> {
            listener.reloadFocusConcept();
        });
    }

    public final void navigateTo(T concept) {
        navigateTo(concept, true);
    }
    
    public void navigateTo(T concept, boolean addHistoryEntry) {
        activeFocusConcept = Optional.of(concept);

        listeners.forEach( (listener) -> {
            listener.focusConceptChanged(concept);
        });
        
        if(addHistoryEntry) {
            history.addHistoryEntry(concept);
            
            if(mainPanel.getWorkspace().isPresent()) {
                mainPanel.getWorkspace().get().getHistory().addHistoryEntry(concept);
            }
        }
    }
    
    public NATBrowserPanel<T> getNATBrowserPanel() {
        return mainPanel;
    }

    public T getActiveFocusConcept() {
        return activeFocusConcept.get();
    }
}

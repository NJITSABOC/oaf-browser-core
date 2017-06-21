package edu.njit.cs.saboc.nat.generic.gui.panels;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.nat.generic.FocusConceptManager.FocusConceptListener;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import javax.swing.SwingUtilities;

/**
 * Base panel for displaying a focus-concept-related result.
 * 
 * @author Chris O
 * @param <T>
 * @param <V>
 */
public abstract class ResultPanel<T extends Concept, V> extends BaseNATPanel<T> {
    
    /**
     * Interface for defining methods that are used to retrieve data
     * related to a focus concept
     * 
     * @param <T>
     * @param <V> 
     */
    public interface DataRetriever<T extends Concept, V> {
        public V getData(T concept);
        
        public String getDataType();
    }
       
    private DataRetriever<T, V> dataRetriever;
    
    private boolean isActive;
    private boolean loaded;
    
    public ResultPanel(
            NATBrowserPanel<T> mainPanel,
            DataRetriever<T, V> dataRetriever) {
        
        super(mainPanel);
        
        this.dataRetriever = dataRetriever;
        
        mainPanel.getFocusConceptManager().addFocusConceptListener(new FocusConceptListener<T>() {
            
            @Override
            public void focusConceptChanged(T concept) {
                loaded = false;

                if (isActive) {
                    doLoad(concept);
                }
            }

            @Override
            public void reloadFocusConcept() {
                reload();
            }
        });
        
        this.isActive = true;
        
        this.loaded = false;
    }
    
    public void setActive(boolean value) {
        this.isActive = value;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    protected void doLoad(T concept) {
        
        if(!isActive) {
            return;
        }
        
        if(loaded) {
            return;
        }
        
        this.loaded = true;
        
        dataPending();

        Thread loadThread = new Thread(() -> {
            V result = dataRetriever.getData(concept);

            SwingUtilities.invokeLater(() -> {
                displayResults(result);
            });
        });

        loadThread.start();
    }
    
    /**
     * Reloads the data displayed in the result panel, if the panel is active
     */
    protected void reload() {        
        doLoad(getMainPanel().getFocusConceptManager().getActiveFocusConcept());
    }
    
    /**
     * Forces the panel to reload the data, even if its not active
     */
    protected void forceReload() {
        this.loaded = false;
        
        reload();
    }
    
    /**
     * Set a different data retriever for that same result type
     * 
     * @param dataRetriever 
     */
    public void setDataRetriever(DataRetriever<T, V> dataRetriever) {
        this.dataRetriever = dataRetriever;
        
        if(isActive) {
            forceReload();
        }
    }
    
    public DataRetriever<T, V> getCurrentDataRetriever() {
        return dataRetriever;
    }

    public abstract void dataPending();
    
    public abstract void displayResults(V data);
}

package edu.njit.cs.saboc.nat.generic;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFStateFileManager;
import edu.njit.cs.saboc.nat.generic.data.ConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditDatabase;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSet;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSetLoader;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSetLoaderException;
import edu.njit.cs.saboc.nat.generic.gui.layout.NATLayout;
import edu.njit.cs.saboc.nat.generic.history.BookmarkManager;
import edu.njit.cs.saboc.nat.generic.history.FocusConceptHistory;
import edu.njit.cs.saboc.nat.generic.workspace.NATWorkspace;
import edu.njit.cs.saboc.nat.generic.workspace.NATWorkspaceManager;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main panel of the NAT. Also maintains a reference to the 
 * current data source, the focus concept manager for this instance
 * of the NAT, and the audit data base for the current instance of the NAT.
 * 
 * @author Chris O
 * 
 * @param <T>
 */
public class NATBrowserPanel<T extends Concept> extends JPanel {

    private Optional<ConceptBrowserDataSource<T>> optDataSource;

    private final FocusConceptManager<T> focusConceptManager;
    
    private final JFrame parentFrame;
    
    private final OAFStateFileManager stateFileManager;
     
    private final NATLayout<T> layout;
    
    private final AuditDatabase<T> auditDatabase;
    
    private final BookmarkManager<T> bookmarkManager;
    
    private Optional<NATWorkspace<T>> optWorkspace;
    
    private final ArrayList<DataSourceChangeListener<T>> dataSourceChangeListeners;
    
    public NATBrowserPanel(
            JFrame parentFrame, 
            OAFStateFileManager stateFileManager,
            NATLayout layout) {

        this.optDataSource = Optional.empty();
        
        this.parentFrame = parentFrame;
        this.stateFileManager = stateFileManager;
        
        this.layout = layout;
                
        this.setLayout(new BorderLayout());
                      
        this.add(layout, BorderLayout.CENTER);    
        
        this.focusConceptManager = new FocusConceptManager<>(this);
        
        this.auditDatabase = new AuditDatabase<>(this);
        
        this.auditDatabase.addAuditDatabaseChangeListener( () -> {
            focusConceptManager.refresh();
        });
        
        this.bookmarkManager = new BookmarkManager<>();
        
        this.optWorkspace = Optional.empty();
        
        this.dataSourceChangeListeners = new ArrayList<>();
        
        layout.createLayout(this);
        
        this.revalidate();
        this.repaint();
    }
    
    public void addDataSourceChangeListener(DataSourceChangeListener<T> listener) {
        this.dataSourceChangeListeners.add(listener);
    }
    
    public void removeDataSourceChangeListener(DataSourceChangeListener<T> listener) {
        this.dataSourceChangeListeners.remove(listener);
    }
    
    public void setDataSource(ConceptBrowserDataSource<T> dataSource) {
        this.optDataSource = Optional.of(dataSource);
        
        this.dataSourceChangeListeners.forEach( (listener) -> {
            listener.dataSourceLoaded(dataSource);
        });
    }
    
    public void clearDataSource() {
        this.optDataSource = Optional.empty();

        this.dataSourceChangeListeners.forEach((listener) -> {
            listener.dataSourceRemoved();
        });
    }
    
    public OAFStateFileManager getStateFileManager() {
        return stateFileManager;
    }
    
    public Optional<ConceptBrowserDataSource<T>> getDataSource() {
        return optDataSource;
    }
    
    public FocusConceptManager<T> getFocusConceptManager() {
        return focusConceptManager;
    }
    
    public BookmarkManager<T> getBookmarkManager() {
        return bookmarkManager;
    }
    
    public AuditDatabase<T> getAuditDatabase() {
        return auditDatabase;
    }

    public void navigateTo(T c) {
        focusConceptManager.navigateTo(c);
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }
    
    public NATLayout<T> getNATLayout() {
        return layout;
    }
    
    public Optional<NATWorkspace<T>> getWorkspace() {
        return optWorkspace;
    }
    
    public NATWorkspaceManager getWorkspaceManager() {
        return new NATWorkspaceManager(this, optDataSource.get().getRecentlyOpenedWorkspaces());
    }
    
    public void setWorkspace(NATWorkspace<T> workspace) {
        
        if(!this.getDataSource().isPresent()) {
            return;
        }
        
        this.optWorkspace = Optional.of(workspace);
        
        FocusConceptHistory<T> history = this.focusConceptManager.getHistory();
        
        history.setHistory(workspace.getHistory().getHistory());
     
        this.getFocusConceptManager().navigateTo(
                history.getHistory().get(history.getPosition()).getConcept(), 
                
                false);
        
        if(workspace.getAuditSet().isPresent()) {
            try {
                AuditSet<T> auditSet = AuditSetLoader.createAuditSetFromJSON(workspace.getAuditSet().get(), this.getDataSource().get());
                
                this.getAuditDatabase().setAuditSet(auditSet);
            } catch (AuditSetLoaderException asle) {

            }
        }
    }
    
    public void clearWorkspace() {
        this.optWorkspace = Optional.empty();
    }
    
    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        
        this.layout.setEnabled(value);
    }
    
    public void reset() {
        this.layout.reset();

        clearWorkspace();
        clearDataSource();

        this.getAuditDatabase().clearLoadedAuditSet();
        this.getBookmarkManager().setBookmarks(new ArrayList<>());
        this.getFocusConceptManager().getHistory().setHistory(new ArrayList<>());
    }
}

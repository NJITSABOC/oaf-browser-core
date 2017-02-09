package edu.njit.cs.saboc.nat.generic.gui.panels.history;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.FilterableList;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.data.ConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.gui.filterablelist.FilterableFocusConceptHistoryEntry;
import edu.njit.cs.saboc.nat.generic.gui.filterablelist.renderer.HistoryEntryRenderer;
import edu.njit.cs.saboc.nat.generic.gui.panels.NATLayoutPanel;
import edu.njit.cs.saboc.nat.generic.history.FocusConceptHistory.HistoryListener;
import edu.njit.cs.saboc.nat.generic.history.FocusConceptHistoryEntry;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Chris O
 * @param <T>
 */
public class HistoryPanel<T extends Concept> extends NATLayoutPanel {
    
    private class HistoryEntryList<T extends Concept> extends FilterableList<FocusConceptHistoryEntry<T>> {
        
        private final NATBrowserPanel<T> mainPanel;
        private final ConceptBrowserDataSource<T> dataSource;
        
        public HistoryEntryList(
            NATBrowserPanel<T> mainPanel, 
            ConceptBrowserDataSource<T> dataSource) {
            
            this.mainPanel = mainPanel;
            this.dataSource = dataSource;
            
            super.addListMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                        if(!getSelectedValues().isEmpty()) {
                            FocusConceptHistoryEntry<T> result = getSelectedValues().get(0).getObject();
                            
                            mainPanel.getFocusConceptManager().navigateTo(result.getConcept());
                        }
                    }
                }
            });
            
            super.setListCellRenderer(new HistoryEntryRenderer(mainPanel));
        }
        
        public void updateHistoryDisplay() {
            ArrayList<FocusConceptHistoryEntry<T>> historyEntries = 
                    new ArrayList<>(mainPanel.getFocusConceptManager().getHistory().getHistory());
            
            Collections.reverse(historyEntries);
            
            ArrayList<FilterableFocusConceptHistoryEntry<T>> filterableEntries = new ArrayList<>();
            
            historyEntries.forEach( (entry) -> {
                filterableEntries.add(new FilterableFocusConceptHistoryEntry<>(entry, dataSource));
            });

            super.setContents(filterableEntries);
        }
    }

    private final HistoryEntryList<T> historyList;
    
    private final NATBrowserPanel<T> mainPanel;
    private final ConceptBrowserDataSource<T> dataSource;

    public HistoryPanel(
            NATBrowserPanel<T> mainPanel, 
            ConceptBrowserDataSource<T> dataSource) {
        
        super(mainPanel);
        
        this.mainPanel = mainPanel;
        this.dataSource = dataSource;
        
        this.historyList = new HistoryEntryList<>(mainPanel, dataSource);
        
        this.setLayout(new BorderLayout());
        
        this.add(historyList, BorderLayout.CENTER);
        
        mainPanel.getFocusConceptManager().getHistory().addHistoryListener(new HistoryListener() {

            @Override
            public void historyEntryAdded() {
                historyList.updateHistoryDisplay();
            }

            @Override
            public void historyBack() {
                historyList.updateHistoryDisplay();
            }

            @Override
            public void historyForward() {
                historyList.updateHistoryDisplay();
            }
        });
    }

    @Override
    protected void setFontSize(int fontSize) {
        
    }
}
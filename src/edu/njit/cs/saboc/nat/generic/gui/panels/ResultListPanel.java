package edu.njit.cs.saboc.nat.generic.gui.panels;

import edu.njit.cs.saboc.blu.core.gui.iconmanager.ImageManager;
import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.Filterable;
import edu.njit.cs.saboc.blu.core.utils.filterable.list.FilterableList;
import edu.njit.cs.saboc.blu.core.utils.rightclickmanager.EntityRightClickManager;
import edu.njit.cs.saboc.blu.core.utils.rightclickmanager.EntityRightClickMenuGenerator;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.listeners.DataLoadedListener;
import edu.njit.cs.saboc.nat.generic.gui.panels.ResultPanel.DataRetriever;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * A result panel for displaying a list of results.
 * 
 * @author Chris O
 * 
 * @param <T>
 * @param <V>
 */
public abstract class ResultListPanel<T extends Concept, V> extends ResultPanel<T, ArrayList<V>> {
    
    public interface ResultSelectedListener<V> {
        public void resultSelected(V result);
        public void noResultSelected();
    }
 
    private final FilterableList<V> list;
 
    private final JPanel optionsPanel;
    
    private final ArrayList<ResultSelectedListener<V>> resultSelectedListeners = new ArrayList<>();
    
    private final ArrayList<DataLoadedListener<ArrayList<V>>> dataLoadedListeners = new ArrayList<>();
    
    private final EntityRightClickManager<V> rightClickManager = new EntityRightClickManager<>();
    
    private final boolean showBorder;
    
    private final JButton filterButton;
    
    public ResultListPanel(
            NATBrowserPanel<T> mainPanel,
            DataRetriever<T, ArrayList<V>> dataRetriever,
            ListCellRenderer<Filterable<V>> renderer,
            boolean showFilter,
            boolean showBorder) {
        
        super(mainPanel, dataRetriever);
        
        this.showBorder = showBorder;

        this.list = new FilterableList<>();
        
        this.list.setListCellRenderer(renderer);
        
        this.list.addListMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    List<Filterable<V>> selectedValues = list.getSelectedValues();

                    if (!selectedValues.isEmpty()) {
                        Filterable<V> value = selectedValues.get(0);

                        resultSelectedListeners.forEach((listener) -> {
                            listener.resultSelected(value.getObject());
                        });
                    }
                }
                
                if (e.getButton() == MouseEvent.BUTTON3){
                    int index = list.locationToIndex(e);
                    
                    if(index >= 0) {
                        V item = list.getItemAtIndex(index).getObject();
                        
                        list.setSelectedIndex(index);
    
                        rightClickManager.setRightClickedItem(item);
                    } else {
                        rightClickManager.clearRightClickedItem();
                    }
                    
                    rightClickManager.showPopup(e);
                }
            }
        });
        
        this.optionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        setLayout(new BorderLayout());
        
        this.add(optionsPanel, BorderLayout.NORTH);
        this.add(list, BorderLayout.CENTER);
        
        FlowLayout buttonLayout = new FlowLayout(FlowLayout.TRAILING);
        buttonLayout.setHgap(0);
        
        this.filterButton = new JButton();

        filterButton.setPreferredSize(new Dimension(24, 24));
        filterButton.setIcon(ImageManager.getImageManager().getIcon("filter.png"));
        filterButton.setToolTipText("Filter these entries");
        filterButton.addActionListener((ae) -> {
            list.setFilterPanelOpen(!list.filterShowing());
        });

        if (showFilter) {
            this.addOptionsComponent(filterButton);
        }
        
        updateBorder("Initializing...");
    }
    
    public final void addOptionsComponent(JComponent component) {
        optionsPanel.setVisible(true);
        optionsPanel.add(component, 0);
        optionsPanel.add(Box.createHorizontalStrut(16), 0);
    }
    
    public final void setRightClickMenuGenerator(EntityRightClickMenuGenerator<V> generator) {
        this.rightClickManager.setMenuGenerator(generator);
    }
    
    public final void clearRightClickMenuGenerator() {
        this.rightClickManager.clearMenuGenerator();
    }

    public void addDataLoadedListener(DataLoadedListener<ArrayList<V>> listener) {
        this.dataLoadedListeners.add(listener);
    }
    
    public void removeDataLoadedListener(DataLoadedListener<ArrayList<V>> listener) {
        this.dataLoadedListeners.remove(listener);
    }
    
    public void addResultSelectedListener(ResultSelectedListener<V> listener) {
        this.resultSelectedListeners.add(listener);
    }

    public void removeResultSelectedListener(ResultSelectedListener<V> listener) {
        this.resultSelectedListeners.remove(listener);
    }

    @Override
    public void dataPending() {
        list.showPleaseWait();
        
        optionsPanel.setEnabled(false);
        
        updateBorder("Loading...");
    }

    @Override
    public void displayResults(ArrayList<V> data) {
        ArrayList<Filterable<V>> entries = new ArrayList<>();

        data.forEach( (result) -> {
            entries.add(createFilterableEntry(result));
        });

        list.setContents(entries);
        
        dataLoadedListeners.forEach( (listener) -> {
            listener.dataLoaded(data);
        });
        
        optionsPanel.setEnabled(true);
        
        updateBorder(Integer.toString(data.size()));
    }
    
    private void updateBorder(String status) {
        if(showBorder) {
            this.setBorder(this.createTitltedBorder(getCurrentDataRetriever().getDataType(), status));
        }
    }
    
    /**
     * Create the filterable entry for the type of data being displayed in the 
     * filterable list
     * 
     * @param entry
     * @return 
     */
    protected abstract Filterable<V> createFilterableEntry(V entry);
    
    private TitledBorder createTitltedBorder(String type, String countOrStatus) {
        
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), 
                String.format("%s (%s)", type, countOrStatus));
    }
    
    
    @Override
    public void setEnabled(boolean value) {
        
        super.setEnabled(value);
        
        this.list.setEnabled(value);
        this.filterButton.setEnabled(value);
        
        List<Component> components = Arrays.asList(this.optionsPanel.getComponents());
        
        components.forEach( (component) -> {
            component.setEnabled(value);
        });
    }
    
    @Override
    public void reset() {
        this.dataPending();
    }
    
    @Override
    protected void reload() {
        int scrollLocation = this.list.getVerticalScrollLocation();
        
        super.reload();
        
        SwingUtilities.invokeLater(() -> {
            this.list.setVerticalScrollLocation(scrollLocation);
        });
    }
}

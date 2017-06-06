package edu.njit.cs.saboc.nat.generic.gui.layout;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

/**
 * A layout that separates the NAT display into three adjustable columns 
 * 
 * @author Chris
 * @param <T>
 */
public abstract class BaseNATAdjustableLayout<T extends Concept> extends NATLayout<T> {
    
    private final JPanel leftPanel;
    private final JPanel midPanel;
    private final JPanel rightPanel;
    
    private NATBrowserPanel<T> browserPanel;

    public BaseNATAdjustableLayout() {
        super();
        
        this.leftPanel = new JPanel(new BorderLayout());
        this.midPanel = new JPanel(new BorderLayout());
        this.rightPanel = new JPanel(new BorderLayout());
    }
    
    @Override
    public void createLayout(NATBrowserPanel<T> mainPanel) {
        
        this.browserPanel = mainPanel;
        
        JSplitPane leftPane = BaseNATAdjustableLayout.createStyledSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftPane.setDividerLocation(400);
        
        leftPane.setLeftComponent(leftPanel);
        
        JSplitPane rightPane = BaseNATAdjustableLayout.createStyledSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        rightPane.setDividerLocation(500);
        rightPane.setLeftComponent(midPanel);
        rightPane.setRightComponent(rightPanel);
        
        leftPane.setRightComponent(rightPane);
        
        this.setLayout(new BorderLayout());

        this.add(leftPane, BorderLayout.CENTER);

        mainPanel.add(this);
    }
    
    public NATBrowserPanel<T> getMainPanel() {
        return browserPanel;
    }
    
    protected void setLeftPanelContents(JComponent panel) {
        this.leftPanel.add(panel, BorderLayout.CENTER);
    }
    
    protected void setMiddlePanelContents(JComponent panel) {
        this.midPanel.add(panel, BorderLayout.CENTER);
    }
    
    protected void setRightPanelContents(JComponent panel) {
        this.rightPanel.add(panel, BorderLayout.CENTER);
    }
    
    public static JSplitPane createStyledSplitPane(int alignment) {
        JSplitPane splitPane = new JSplitPane(alignment);
        splitPane.setBorder(null);

        Optional<BasicSplitPaneDivider> divider = Optional.empty();

        for (Component c : splitPane.getComponents()) {
            if (c instanceof BasicSplitPaneDivider) {
                divider = Optional.of((BasicSplitPaneDivider) c);
                break;
            }
        }

        if (divider.isPresent()) {
            divider.get().setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            divider.get().setDividerSize(4);
        }
        
        return splitPane;
    }
}

package edu.njit.cs.saboc.nat.generic;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.utils.toolstate.OAFStateFileManager;
import edu.njit.cs.saboc.nat.generic.data.ConceptBrowserDataSource;
import edu.njit.cs.saboc.nat.generic.gui.layout.NATLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 * Generic frame for displaying a NAT browser panel in the OAF 
 * system.
 * 
 * @author Chris
 * @param <T>
 */
public class NATBrowserFrame<T extends Concept> extends JInternalFrame {

    private final NATBrowserPanel<T> browser;
    
    public NATBrowserFrame(
            JFrame parentFrame, 
            OAFStateFileManager stateFileManager,
            ConceptBrowserDataSource<T> dataSource,
            NATLayout layout) {
        
        super("Ontology Abstraction Framework (OAF) Neighborhood Auditing Tool (NAT)", 
                true, 
                true, 
                true,
                false);
        
        this.browser = new NATBrowserPanel<>(parentFrame, stateFileManager, layout);
        this.browser.setDataSource(dataSource);
        
        this.setSize(1400, 600);
        
        this.add(browser);
        
        this.setVisible(true);
    }

    public void nagivateTo(T c) {
        browser.navigateTo(c);
    }
}

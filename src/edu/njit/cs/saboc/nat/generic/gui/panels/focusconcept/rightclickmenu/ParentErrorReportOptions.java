
package edu.njit.cs.saboc.nat.generic.gui.panels.focusconcept.rightclickmenu;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.gui.panels.errorreporting.errorreport.dialog.ErrorReportDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author Chris O
 */
public class ParentErrorReportOptions {
    
    public static <T extends Concept> ArrayList<JComponent> createParentErrorComponents(
            NATBrowserPanel<T> mainPanel,
            T erroneousConcept,
            T parent) {

        ArrayList<JComponent> components = new ArrayList<>();
        
        JMenuItem removeErroneousParentBtn = new JMenuItem("Remove parent (erroneous)");
        removeErroneousParentBtn.setFont(removeErroneousParentBtn.getFont().deriveFont(14.0f));
        removeErroneousParentBtn.addActionListener((ae) -> {
            ErrorReportDialog.displayErroneousParentDialog(mainPanel, erroneousConcept, parent);
        });

        JMenuItem removeRedundantParentBtn = new JMenuItem("Remove parent (redundant)");
        removeRedundantParentBtn.setFont(removeRedundantParentBtn.getFont().deriveFont(14.0f));
        removeRedundantParentBtn.addActionListener((ae) -> {
            ErrorReportDialog.displayRedundantParentErrorDialog(mainPanel, erroneousConcept, parent);
        });

        JMenuItem otherParentErrorBtn = new JMenuItem("Other error with parent");
        otherParentErrorBtn.setFont(otherParentErrorBtn.getFont().deriveFont(14.0f));
        otherParentErrorBtn.addActionListener((ae) -> {
            ErrorReportDialog.displayOtherParentErrorDialog(mainPanel, erroneousConcept, parent);
        });

        JMenuItem replaceParentBtn = new JMenuItem("Replace erroneous parent");
        replaceParentBtn.setFont(replaceParentBtn.getFont().deriveFont(14.0f));
        replaceParentBtn.addActionListener((ae) -> {
            ErrorReportDialog.displayReplaceParentDialog(mainPanel, erroneousConcept, parent);
        });

        
        JPanel otherErrorPanel = new JPanel(new BorderLayout());
        JLabel otherErrorLabel = new JLabel(" Other Error");
        otherErrorLabel.setFont(otherErrorLabel.getFont().deriveFont(16.0f));

        otherErrorPanel.setBackground(Color.WHITE);
        otherErrorPanel.setOpaque(true);
        otherErrorPanel.add(otherErrorLabel, BorderLayout.CENTER);
        
        JMenuItem reportMissingParentBtn = new JMenuItem("Missing parent");
        reportMissingParentBtn.setFont(reportMissingParentBtn.getFont().deriveFont(14.0f));
        reportMissingParentBtn.addActionListener((ae) -> {
            ErrorReportDialog.displayMissingParentDialog(mainPanel, erroneousConcept);
        });

        components.add(removeErroneousParentBtn);
        components.add(removeRedundantParentBtn);
        components.add(otherParentErrorBtn);
        components.add(replaceParentBtn);

        components.add(new JSeparator());
        components.add(otherErrorPanel);
        components.add(new JSeparator());

        components.add(reportMissingParentBtn);
        
        return components;
    }

}

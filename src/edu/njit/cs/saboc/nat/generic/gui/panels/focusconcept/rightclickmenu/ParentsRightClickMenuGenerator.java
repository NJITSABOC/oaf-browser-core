package edu.njit.cs.saboc.nat.generic.gui.panels.focusconcept.rightclickmenu;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSet;
import edu.njit.cs.saboc.nat.generic.errorreport.error.parent.ParentError;
import edu.njit.cs.saboc.nat.generic.gui.panels.errorreporting.errorreport.dialog.ErrorReportDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/**
 *
 * @author Chris O
 * @param <T>
 */
public class ParentsRightClickMenuGenerator<T extends Concept> extends AuditReportRightClickMenu<T, T> {
    
    private final NATBrowserPanel<T> mainPanel;
    
    public ParentsRightClickMenuGenerator(NATBrowserPanel<T> mainPanel) {
        this.mainPanel = mainPanel;
    }

    @Override
    public ArrayList<JComponent> buildRightClickMenuFor(T item) {
        
        if (mainPanel.getAuditDatabase().getLoadedAuditSet().isPresent()) {

            AuditSet<T> auditSet = mainPanel.getAuditDatabase().getLoadedAuditSet().get();
            T focusConcept = mainPanel.getFocusConceptManager().getActiveFocusConcept();

            ArrayList<JComponent> components = new ArrayList<>();

            JPanel namePanel = new JPanel(new BorderLayout());
            namePanel.add(Box.createHorizontalStrut(8), BorderLayout.WEST);
            namePanel.add(Box.createHorizontalStrut(8), BorderLayout.EAST);
            
            JLabel nameLabel = new JLabel("Erroneous Parent: " + item.getName());
            nameLabel.setFont(nameLabel.getFont().deriveFont(16.0f));
            
            namePanel.setBackground(Color.WHITE);
            namePanel.setOpaque(true);
            
            namePanel.add(nameLabel, BorderLayout.CENTER);
            
            components.add(namePanel);
            
            components.add(new JSeparator());
            
            components.addAll(ParentErrorReportOptions.createParentErrorComponents(mainPanel, focusConcept, item));

            List<ParentError<T>> reportedParentErrors = auditSet.getParentErrors(focusConcept);
                        
            if (!reportedParentErrors.isEmpty()) {
                components.add(new JSeparator());

                JPanel currentErrorsPanel = new JPanel(new BorderLayout());
                currentErrorsPanel.add(Box.createHorizontalStrut(8), BorderLayout.WEST);
                currentErrorsPanel.add(Box.createHorizontalStrut(8), BorderLayout.EAST);

                JLabel currentErrorsLabel = new JLabel("Reported Errors");
                currentErrorsLabel.setFont(nameLabel.getFont().deriveFont(16.0f));

                currentErrorsPanel.setBackground(Color.WHITE);
                currentErrorsPanel.setOpaque(true);

                currentErrorsPanel.add(currentErrorsLabel, BorderLayout.CENTER);

                components.add(currentErrorsPanel);

                components.add(new JSeparator());

                components.add(generateRemoveErrorMenu(auditSet, focusConcept, reportedParentErrors));
            }

            return components;
        }

        return new ArrayList<>();
    }

    @Override
    public ArrayList<JComponent> buildEmptyListRightClickMenu() {

        if (mainPanel.getAuditDatabase().getLoadedAuditSet().isPresent()) {

            ArrayList<JComponent> components = new ArrayList<>();

            AuditSet<T> auditSet = mainPanel.getAuditDatabase().getLoadedAuditSet().get();
            T focusConcept = mainPanel.getFocusConceptManager().getActiveFocusConcept();
            
            JPanel otherErrorPanel = new JPanel(new BorderLayout());
            JLabel nameLabel = new JLabel(" Other Error");
            nameLabel.setFont(nameLabel.getFont().deriveFont(16.0f));

            otherErrorPanel.setBackground(Color.WHITE);
            otherErrorPanel.setOpaque(true);
            otherErrorPanel.add(nameLabel, BorderLayout.CENTER);

            JMenuItem reportMissingParent = new JMenuItem("Missing parent");
            reportMissingParent.setFont(reportMissingParent.getFont().deriveFont(14.0f));
            reportMissingParent.addActionListener((ae) -> {
                ErrorReportDialog.displayMissingParentDialog(mainPanel, focusConcept);
            });

            components.add(otherErrorPanel);
            components.add(new JSeparator());

            components.add(reportMissingParent);

            List<ParentError<T>> reportedParentErrors = auditSet.getParentErrors(focusConcept);

            if (!reportedParentErrors.isEmpty()) {
                components.add(new JSeparator());

                components.add(generateRemoveErrorMenu(auditSet, focusConcept, reportedParentErrors));
            }

            return components;
        }
        
        return new ArrayList<>();
    }
    
}

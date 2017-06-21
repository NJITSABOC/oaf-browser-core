package edu.njit.cs.saboc.nat.generic.gui.panels.focusconcept.rightclickmenu;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.nat.generic.NATBrowserPanel;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSet;
import edu.njit.cs.saboc.nat.generic.errorreport.error.child.ChildError;
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
public class ChildrenRightClickMenu<T extends Concept> extends AuditReportRightClickMenu<T, T> {
    
    private final NATBrowserPanel<T> mainPanel;
    
    public ChildrenRightClickMenu(NATBrowserPanel<T> mainPanel) {
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
            
            JLabel nameLabel = new JLabel("Erroneous Child: " + item.getName());
            nameLabel.setFont(nameLabel.getFont().deriveFont(16.0f));
            
            namePanel.setBackground(Color.WHITE);
            namePanel.setOpaque(true);
            
            namePanel.add(nameLabel, BorderLayout.CENTER);
            
            components.add(namePanel);
            
            components.add(new JSeparator());
            
            JMenuItem removeErroneousChildBtn = new JMenuItem("Remove child (erroneous)");
            removeErroneousChildBtn.setFont(removeErroneousChildBtn.getFont().deriveFont(14.0f));
            removeErroneousChildBtn.addActionListener((ae) -> {
                ErrorReportDialog.displayErroneousChildDialog(mainPanel, focusConcept, item);
            });

            
            JMenuItem otherErrorBtn = new JMenuItem("Other error with child");
            otherErrorBtn.setFont(otherErrorBtn.getFont().deriveFont(14.0f));
            otherErrorBtn.addActionListener((ae) -> {
                ErrorReportDialog.displayOtherChildErrorDialog(mainPanel, focusConcept, item);
            });
            
            JMenuItem missingChildBtn = new JMenuItem("Missing child");
            missingChildBtn.setFont(missingChildBtn.getFont().deriveFont(14.0f));
            missingChildBtn.addActionListener((ae) -> {
                ErrorReportDialog.displayMissingChildDialog(mainPanel, focusConcept);
            });
            
            components.add(removeErroneousChildBtn);
            
            components.add(otherErrorBtn);
            
            components.add(new JSeparator());
            
            JPanel otherErrorPanel = new JPanel(new BorderLayout());
            JLabel otherErrorLabel = new JLabel(" Other Error");
            otherErrorLabel.setFont(otherErrorLabel.getFont().deriveFont(16.0f));

            otherErrorPanel.setBackground(Color.WHITE);
            otherErrorPanel.setOpaque(true);
            otherErrorPanel.add(otherErrorLabel, BorderLayout.CENTER);

            components.add(otherErrorPanel);
            components.add(new JSeparator());
            
            components.add(missingChildBtn);

            List<ChildError<T>> reportedChildErrors = auditSet.getChildErrors(focusConcept);
                        
            if(!reportedChildErrors.isEmpty()) {
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
                
                components.add(generateRemoveErrorMenu(auditSet, focusConcept, reportedChildErrors));
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
            
            JMenuItem reportMissingChild = new JMenuItem("Missing child");
            reportMissingChild.setFont(reportMissingChild.getFont().deriveFont(14.0f));
            reportMissingChild.addActionListener((ae) -> {
                ErrorReportDialog.displayMissingChildDialog(mainPanel, focusConcept);
            });
            
            components.add(otherErrorPanel);
            components.add(new JSeparator());
            components.add(reportMissingChild);

            List<ChildError<T>> reportedChildErrors = auditSet.getChildErrors(focusConcept);
                        
            if(!reportedChildErrors.isEmpty()) {
                components.add(new JSeparator());
                
                components.add(generateRemoveErrorMenu(auditSet, focusConcept, reportedChildErrors));
            }
            
            return components;
        }
        
        return new ArrayList<>();
    }    
}
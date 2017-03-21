package edu.njit.cs.saboc.nat.generic.gui.panels.focusconcept.rightclickmenu;

import edu.njit.cs.saboc.blu.core.ontology.Concept;
import edu.njit.cs.saboc.blu.core.utils.rightclickmanager.EntityRightClickMenuGenerator;
import edu.njit.cs.saboc.nat.generic.errorreport.AuditSet;
import edu.njit.cs.saboc.nat.generic.errorreport.error.OntologyError;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 *
 * @author Chris O
 * @param <T>
 */
public abstract class AuditReportRightClickMenu<T extends Concept> extends EntityRightClickMenuGenerator<T> {

    protected JMenu generateRemoveErrorMenu(
            AuditSet<T> auditSet,
            T focusConcept,
            List<? extends OntologyError<T>> reportedErrors) {

        JMenu removeExistingErrorsMenu = new JMenu("Remove reported error");
        removeExistingErrorsMenu.setFont(removeExistingErrorsMenu.getFont().deriveFont(14.0f));

        reportedErrors.forEach( (error) -> {
            JMenuItem errorBtn = new JMenuItem(error.getStyledText());
            errorBtn.setFont(errorBtn.getFont().deriveFont(14.0f));
            errorBtn.addActionListener((ae) -> {
                auditSet.deleteError(focusConcept, error);
            });

            removeExistingErrorsMenu.add(errorBtn);
        });
        
        removeExistingErrorsMenu.add(new JSeparator());
        
        JMenuItem removeAllBtn = new JMenuItem("Remove all errors");
        removeAllBtn.setFont(removeAllBtn.getFont().deriveFont(14.0f));
        removeAllBtn.addActionListener((ae) -> {
            reportedErrors.forEach( (error) -> {
                auditSet.deleteError(focusConcept, error);
            });
        });

        removeExistingErrorsMenu.add(removeAllBtn);
        
        return removeExistingErrorsMenu;
    }

}
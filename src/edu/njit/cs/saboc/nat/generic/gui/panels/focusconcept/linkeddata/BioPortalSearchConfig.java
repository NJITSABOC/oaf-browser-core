package edu.njit.cs.saboc.nat.generic.gui.panels.focusconcept.linkeddata;

import edu.njit.cs.saboc.blu.core.gui.iconmanager.ImageManager;
import javax.swing.ImageIcon;

/**
 *
 * @author Chris Ochs
 */
public class BioPortalSearchConfig implements OpenBrowserButton.OpenBrowserButtonConfig {

    @Override
    public ImageIcon getIcon() {
        return ImageManager.getImageManager().getIcon("BioPortalIcon.png");
    }

    @Override
    public String getToolTipText() {
        return "NCBO BioPortal Search";
    }

    @Override
    public String getQueryURL() {
        return "https://bioportal.bioontology.org/search?query=";
    }
}

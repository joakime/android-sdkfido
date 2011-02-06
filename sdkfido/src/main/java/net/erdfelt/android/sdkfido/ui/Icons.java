package net.erdfelt.android.sdkfido.ui;

import java.net.URL;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public final class Icons {
    private static final Logger LOG = Logger.getLogger(Icons.class.getName());

    public static Icon getResource(String resource) {
        URL imgURL = Icons.class.getResource(resource);
        if (imgURL == null) {
            LOG.warning("Couldn't find icon: " + resource);
            return null;
        }

        return new ImageIcon(imgURL);
    }
}

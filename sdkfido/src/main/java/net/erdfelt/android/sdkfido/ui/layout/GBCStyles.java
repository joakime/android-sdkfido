package net.erdfelt.android.sdkfido.ui.layout;

import java.util.HashMap;
import java.util.Map;


public class GBCStyles {
    private static final String DEFAULT_KEY = "*";

    private Map<String, GBC> gbcDefaults = new HashMap<String, GBC>();

    public GBCStyles() {
        /* set to private to prevent instantiation */
        gbcDefaults.put(DEFAULT_KEY, new GBC());
    }

    public GBC base() {
        return use(DEFAULT_KEY);
    }

    public GBC define() {
        return define(DEFAULT_KEY);
    }

    public GBC define(String key) {
        GBC gbc = gbcDefaults.get(key);
        if (gbc == null) {
            gbc = new GBC();
            gbcDefaults.put(key, gbc);
        }
        return gbc;
    }

    public GBC use(String key) {
        GBC gbc = gbcDefaults.get(key);
        if (gbc == null) {
            throw new IllegalArgumentException("No GBC defaults exist for key [" + key + "]");
        }
        return (GBC) gbc.clone();
    }
}

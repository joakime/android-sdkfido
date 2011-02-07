package net.erdfelt.android.sdkfido.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Prefs {
    private static final Logger LOG = Logger.getLogger(Prefs.class.getName());
    private static Preferences  prefs;

    static {
        prefs = Preferences.userNodeForPackage(Prefs.class);
    }

    public static int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return prefs.get(key, defValue);
    }

    public static void save() {
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            LOG.log(Level.WARNING, "Unable to save preferences", e);
        }
    }

    public static void setInt(String key, int value) {
        prefs.putInt(key, value);
    }

    public static void setString(String key, String value) {
        prefs.put(key, value);
    }
}

/*******************************************************************************
 *    Copyright 2012 - Joakim Erdfelt
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
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

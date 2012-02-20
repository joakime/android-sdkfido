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

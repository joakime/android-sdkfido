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
package net.erdfelt.android.sdkfido.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.LogManager;

import org.apache.commons.io.IOUtils;

public final class Logging {
    public static void config() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource("logging.properties");
        if (url != null) {
            InputStream in = null;
            try {
                in = url.openStream();
                LogManager.getLogManager().readConfiguration(in);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            } finally {
                IOUtils.closeQuietly(in);
            }
        }

        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
    }
}

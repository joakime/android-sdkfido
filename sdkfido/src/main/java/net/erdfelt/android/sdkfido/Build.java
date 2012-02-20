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
package net.erdfelt.android.sdkfido;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public final class Build {
    private static final Logger LOG         = Logger.getLogger(Build.class.getName());
    private static final String GROUP_ID    = "net.erdfelt.android.sdkfido";
    private static final String ARTIFACT_ID = "sdkfido-lib";

    private static String       version;

    public static String getVersion() {
        if (version == null) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String resource = String.format("META-INF/maven/%s/%s/pom.properties", GROUP_ID, ARTIFACT_ID);
            URL url = cl.getResource(resource);
            if (url == null) {
                version = "[DEV]";
            } else {
                InputStream in = null;
                try {
                    in = url.openStream();
                    Properties props = new Properties();
                    props.load(in);
                    version = props.getProperty("version");
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Unable to read: " + url, e);
                    version = "[UNKNOWN]";
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
        }
        return version;
    }

}

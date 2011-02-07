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

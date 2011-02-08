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

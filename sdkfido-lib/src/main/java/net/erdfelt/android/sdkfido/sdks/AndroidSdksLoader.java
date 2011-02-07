package net.erdfelt.android.sdkfido.sdks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.annotations.DigesterLoader;
import org.apache.commons.digester.annotations.DigesterLoaderBuilder;
import org.xml.sax.SAXException;

public final class AndroidSdksLoader {
    private static final Logger LOG = Logger.getLogger(AndroidSdksLoader.class.getName());

    public static AndroidSdks load() throws IOException {
        String resourcePath = "sdks.xml"; // default
        URL url = AndroidSdksLoader.class.getResource(resourcePath);
        if (url == null) {
            throw new FileNotFoundException("Unable to find resource: " + resourcePath);
        }

        return load(url);
    }

    public static AndroidSdks load(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Unable to find file: " + file);
        }

        return load(file.toURI().toURL());
    }

    private static AndroidSdks load(URL url) throws IOException {
        DigesterLoader loader = DigesterLoaderBuilder.byDefaultFactories();
        Digester digester = loader.createDigester(AndroidSdks.class);

        AndroidSdks sdks;
        try {
            sdks = (AndroidSdks) digester.parse(url);
        } catch (SAXException e) {
            LOG.log(Level.WARNING, "Unable to load/parse Resource: " + url, e);
            throw new IOException("Unable to load/parse Resource: " + url, e);
        }

        return sdks;
    }
}

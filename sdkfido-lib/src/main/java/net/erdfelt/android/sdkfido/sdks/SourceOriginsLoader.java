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

public class SourceOriginsLoader {
    private static final Logger LOG = Logger.getLogger(SourceOriginsLoader.class.getName());

    public static SourceOrigins load() throws IOException {
        String resourcePath = "sdks-ng.xml";
        URL url = SourceOriginsLoader.class.getResource(resourcePath);
        if (url == null) {
            throw new FileNotFoundException("Unable to find resource: " + resourcePath);
        }

        return load(url);
    }

    public static SourceOrigins load(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Unable to find file: " + file);
        }
        return load(file.toURI().toURL());
    }

    public static SourceOrigins load(URL url) throws IOException {
        try {
            DigesterLoader loader = DigesterLoaderBuilder.byDefaultFactories();
            Digester digester = loader.createDigester(SourceOrigins.class);
            SourceOrigins origins = (SourceOrigins) digester.parse(url);
            origins.normalize();
            return origins;
        } catch (SAXException e) {
            LOG.log(Level.WARNING, "Unable to load/parse url: " + url, e);
            throw new IOException("Unable to load/parse url: " + url, e);
        }
    }
}

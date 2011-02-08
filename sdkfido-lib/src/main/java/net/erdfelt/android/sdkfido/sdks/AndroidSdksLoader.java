package net.erdfelt.android.sdkfido.sdks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Substitutor;
import org.apache.commons.digester.substitution.MultiVariableExpander;
import org.apache.commons.digester.substitution.VariableSubstitutor;
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

    private static AndroidSdks loadSdks(Digester digester, URL url) throws IOException, SAXException {
        digester.addObjectCreate("android-source", AndroidSdks.class);
        digester.addSetProperties("android-source", "spec-version", "specVersion");
        digester.addCallMethod("android-source/properties/property", "setProperty", 2);
        digester.addCallParam("android-source/properties/property", 0, "name");
        digester.addCallParam("android-source/properties/property", 1);
        digester.addObjectCreate("android-source/sdk", Sdk.class);
        digester.addSetProperties("android-source/sdk");
        digester.addObjectCreate("android-source/sdk/repo", SdkRepo.class);
        digester.addSetProperties("android-source/sdk/repo");
        digester.addCallMethod("android-source/sdk/repo/include", "addInclude", 1);
        digester.addCallParam("android-source/sdk/repo/include", 0);
        digester.addSetNext("android-source/sdk/repo", "addRepo");
        digester.addSetNext("android-source/sdk", "addSdk");

        return (AndroidSdks) digester.parse(url);
    }

    private static AndroidSdks load(URL url) throws IOException {
        try {
            AndroidSdks source = loadSdks(new Digester(), url);
            MultiVariableExpander expander = new MultiVariableExpander();
            Map<String, Object> exprs = new HashMap<String, Object>();
            for (String key : source.getProperties().keySet()) {
                exprs.put(key, source.getProperty(key));
            }
            expander.addSource("$", exprs);

            Substitutor substitutor = new VariableSubstitutor(expander);
            Digester digester = new Digester();
            digester.setSubstitutor(substitutor);

            return loadSdks(digester, url);
        } catch (SAXException e) {
            LOG.log(Level.WARNING, "Unable to load/parse url: " + url, e);
            throw new IOException("Unable to load/parse url: " + url, e);
        }
    }
}

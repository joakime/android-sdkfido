package net.erdfelt.android.sdkfido.git;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Allow for the local user to specify mirror URLs for specific requested GIT Urls.
 */
public class GitMirrors {
    private static final Logger LOG       = Logger.getLogger(GitMirrors.class.getName());
    private Map<String, String> mirrormap = new HashMap<String, String>();

    public static GitMirrors load(File mirrorxml) {
        if (!mirrorxml.exists()) {
            return new GitMirrors();
        }
        Digester digester = new Digester();
        digester.addObjectCreate("mirrors", GitMirrors.class);

        digester.addCallMethod("mirrors/mirror", "addMirror", 2);
        digester.addCallParam("mirrors/mirror", 0, "url");
        digester.addCallParam("mirrors/mirror", 1, "mirrorurl");

        try {
            return (GitMirrors) digester.parse(mirrorxml);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Unable to load GitMirrors: " + mirrorxml, e);
        } catch (SAXException e) {
            LOG.log(Level.WARNING, "Unable to load GitMirrors: " + mirrorxml, e);
        }
        return new GitMirrors();
    }

    public void addMirror(String originalUrl, String mirrorUrl) {
        this.mirrormap.put(originalUrl, mirrorUrl);
    }

    /**
     * The number of mirror urls
     * 
     * @return the number of mirror urls
     */
    public int size() {
        return mirrormap.size();
    }

    /**
     * Get the new URL for the original URL.
     * 
     * @param originalUrl
     *            the original URL
     * @return the mirror URL if found, or the original URL if no mirror defined.
     */
    public String getNewUrl(String originalUrl) {
        String url = mirrormap.get(originalUrl);
        if (url != null) {
            return url;
        }
        return originalUrl;
    }

}

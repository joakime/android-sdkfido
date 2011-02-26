package net.erdfelt.android.sdkfido.util;

import java.io.File;
import java.net.URI;

import org.apache.commons.io.FilenameUtils;

public final class PathUtil {
    private PathUtil() {
        /* prevent instantiation */
    }

    public static String toRelativePath(File basedir, File destpath) {
        URI baseuri = basedir.toURI();
        URI otheruri = destpath.toURI();
        URI reluri = baseuri.relativize(otheruri);
        return FilenameUtils.separatorsToSystem(reluri.toASCIIString());
    }
}

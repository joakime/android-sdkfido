package net.erdfelt.android.sdkfido.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.util.PropertyExpander;

import org.apache.commons.io.IOUtils;

public class FilteredFileUtil {
    private static final Logger LOG = Logger.getLogger(FilteredFileUtil.class.getName());

    public static void copyWithExpansion(String resourceId, File destFile, Map<String, String> props) {
        URL url = FilteredFileUtil.class.getResource(resourceId);
        if (url == null) {
            LOG.log(Level.WARNING, "Unable to find resourceID: " + resourceId);
            return;
        }

        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader buf = null;
        FileWriter writer = null;
        PrintWriter out = null;
        try {
            in = url.openStream();
            reader = new InputStreamReader(in);
            buf = new BufferedReader(reader);
            writer = new FileWriter(destFile);
            out = new PrintWriter(writer);

            PropertyExpander expander = new PropertyExpander(props);
            String line;

            while ((line = buf.readLine()) != null) {
                out.println(expander.expand(line));
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Unable to open input stream for url: " + url, e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(buf);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }
    }

}

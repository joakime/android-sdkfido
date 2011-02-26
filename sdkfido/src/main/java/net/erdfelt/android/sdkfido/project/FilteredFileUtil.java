package net.erdfelt.android.sdkfido.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.util.PropertyExpander;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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

    public static Document loadExpandedXml(String resourceId, Map<String, String> props) throws OutputProjectException {
        String expanded = loadExpandedAsString(resourceId, props);
        StringReader sr = null;
        try {
            SAXReader sax = new SAXReader(false);
            sr = new StringReader(expanded);
            return sax.read(sr);
        } catch (DocumentException e) {
            throw new OutputProjectException("Unable to parse xml: " + resourceId, e);
        } finally {
            IOUtils.closeQuietly(sr);
        }
    }

    public static String loadExpandedAsString(String resourceId, Map<String, String> props)
            throws OutputProjectException {
        URL url = FilteredFileUtil.class.getResource(resourceId);
        if (url == null) {
            throw new OutputProjectException("Unable to find resourceID: " + resourceId);
        }

        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader buf = null;
        StringWriter writer = null;
        PrintWriter out = null;
        try {
            in = url.openStream();
            reader = new InputStreamReader(in);
            buf = new BufferedReader(reader);
            writer = new StringWriter();
            out = new PrintWriter(writer);

            PropertyExpander expander = new PropertyExpander(props);
            String line;

            while ((line = buf.readLine()) != null) {
                out.println(expander.expand(line));
            }

            out.flush();
            writer.flush();
            return writer.toString();
        } catch (IOException e) {
            throw new OutputProjectException("Unable to open input stream for url: " + url, e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(buf);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(in);
        }
    }

    public static void write(Document pom, File outputFile) throws OutputProjectException {
        XMLWriter writer = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            OutputFormat pretty = OutputFormat.createPrettyPrint();
            pretty.setIndentSize(2);
            writer = new XMLWriter(out, pretty);
            writer.write(pom);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            throw new OutputProjectException("Unable to write xml to file: " + outputFile, e);
        } catch (IOException e) {
            throw new OutputProjectException("Unable to write xml to file: " + outputFile, e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}

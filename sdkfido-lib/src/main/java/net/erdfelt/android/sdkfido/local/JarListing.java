package net.erdfelt.android.sdkfido.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;

/**
 * Represents a Jar Listing.
 */
public class JarListing extends ArrayList<String> {
    private static final long serialVersionUID = 1L;
    private List<String>      classlist        = new ArrayList<String>();

    public JarListing(File jarfile) throws IOException {
        this.clear();

        File altlisting = new File(jarfile.getParentFile(), jarfile.getName() + ".listing");
        if (altlisting.exists()) {
            loadAlt(altlisting);
        } else {
            loadJar(jarfile);
        }
    }

    /**
     * Load listing from text file <code>"${jarfilename}.listing"</code> (useful for unit testing with)
     * 
     * @param altlisting
     * @throws IOException
     */
    private void loadAlt(File altlisting) throws IOException {
        FileReader reader = null;
        BufferedReader buf = null;

        try {
            reader = new FileReader(altlisting);
            buf = new BufferedReader(reader);

            String line;
            while ((line = buf.readLine()) != null) {
                add(line);
                if (line.endsWith(".class")) {
                    classlist.add(line);
                }
            }
        } finally {
            IOUtils.closeQuietly(buf);
            IOUtils.closeQuietly(reader);
        }
    }

    private final void loadJar(File jarfile) throws IOException {
        JarFile jar = null;

        try {
            String name;
            jar = new JarFile(jarfile);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                name = entry.getName();
                add(name);

                if (!entry.isDirectory() && name.endsWith(".class")) {
                    classlist.add(name);
                }
            }
        } finally {
            if (jar != null) {
                jar.close();
            }
        }
    }

    /**
     * Get the list of classes, in jar listing format (eg: "anroid/os/Build.class")
     * 
     * @return the list of classes.
     */
    public List<String> getClassList() {
        return classlist;
    }

    /**
     * Convert all of the classlist entries to *.java source file listings instead.
     * 
     * @return the list of java source files.
     */
    public Set<String> getJavaSourceListing() {
        Set<String> javasrcs = new TreeSet<String>();
        
        String javasrc;
        for(String classname: classlist) {
            if(classname.contains("$")) {
                continue; // skip (inner classes don't have a direct 1::1 java source file
            }
            
            javasrc = classname.replaceFirst("\\.class$", ".java");
            javasrcs.add(javasrc);
        }
        
        return javasrcs;
    }
}

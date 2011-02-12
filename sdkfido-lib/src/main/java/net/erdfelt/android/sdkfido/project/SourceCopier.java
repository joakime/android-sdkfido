package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class SourceCopier {
    private static final Logger LOG         = Logger.getLogger(SourceCopier.class.getName());
    private LinkedList<String>  javalisting = new LinkedList<String>();
    private Project             project;
    private FileWriter          logwriter;
    private PrintWriter         out;
    private int                 found;
    private int                 misses;

    public SourceCopier(Set<String> javalisting) {
        this.javalisting.addAll(javalisting);
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void init() throws IOException {
        File logfile = new File(project.getBaseDir(), Project.COPIED_SOURCE_LOG);
        logwriter = new FileWriter(logfile, false);
        out = new PrintWriter(logwriter);

        // Remove entries that already exist.
        out.println("Removing previously found java source files (already present in tree)");
        ListIterator<String> iterlisting = javalisting.listIterator();
        File searchFile;
        String javafilename;
        while (iterlisting.hasNext()) {
            javafilename = iterlisting.next();
            searchFile = project.getSrcJava(javafilename);
            if (searchFile.exists()) {
                iterlisting.remove();
                out.println("[FOUND] " + javafilename);
            }
        }
    }

    public void searchTree(File gitDir, String include) throws IOException {
        File searchDir = new File(gitDir, toOS(include));
        log("Searching: " + searchDir);

        File searchFile, destFile;
        ListIterator<String> iterlisting = javalisting.listIterator();
        while (iterlisting.hasNext()) {
            String javafilename = iterlisting.next();
            searchFile = new File(searchDir, toOS(javafilename));
            if (searchFile.exists()) {
                destFile = project.getSrcJava(javafilename);
                FileUtils.copyFile(searchFile, destFile);
                iterlisting.remove(); // Don't search for this one again.
                out.println("[FOUND] " + javafilename);
                found++;
            } else {
                misses++;
            }
        }
    }

    @Override
    public String toString() {
        return "Copied " + found + " files, missed " + misses + " files (only " + javalisting.size()
                + " left to find!)";
    }

    private void log(String msg) {
        LOG.info(msg);
        out.println(msg);
        out.flush();
    }

    private String toOS(String path) {
        return FilenameUtils.separatorsToSystem(path);
    }

    public void close() {
        for (String javafilename : javalisting) {
            out.println("[NOT FOUND] " + javafilename);
        }

        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(logwriter);
    }
}

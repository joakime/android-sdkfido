package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
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
    private int                 countCopied;
    private int                 countHits;
    private int                 countExtras;
    private int                 countResources;

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
        updateSearchListings();
    }

    private void updateSearchListings() {
        File searchFile;
        String javafilename;
        ListIterator<String> iterlisting = javalisting.listIterator();
        while (iterlisting.hasNext()) {
            javafilename = iterlisting.next();
            searchFile = project.getSrcJava(javafilename);
            if (searchFile.exists()) {
                iterlisting.remove();
                out.println("[FOUND] " + javafilename);
            }
        }
    }

    public void copyTree(File gitDir, String include) throws IOException {
        File searchDir = new File(gitDir, toOS(include));
        if (searchDir.exists()) {
            log("Copying Tree: " + searchDir);
            copyDirectory(searchDir);
            updateSearchListings();
        }
    }

    public void copyDirectory(File basedir) throws IOException {
        recurseDirCopy(basedir, basedir);
    }

    public void recurseDirCopy(File basedir, File dir) throws IOException {
        String name, relpath;
        LOG.info("Recurse Copy: " + dir);

        for (File path : dir.listFiles()) {
            if (path.isDirectory()) {
                recurseDirCopy(basedir, path);
            } else if (path.isFile()) {
                relpath = toRelativePath(basedir, path);
                name = path.getName();
                countCopied++;
                if (name.endsWith(".java")) {
                    if (javalisting.contains(relpath)) {
                        countHits++;
                    } else {
                        countExtras++;
                    }
                    FileUtils.copyFile(path, project.getSrcJava(relpath));
                } else {
                    countResources++;
                    FileUtils.copyFile(path, project.getSrcResource(relpath));
                }
            }
        }
    }

    public static String toRelativePath(File basedir, File destpath) {
        URI baseuri = basedir.toURI();
        URI otheruri = destpath.toURI();
        URI reluri = baseuri.relativize(otheruri);
        return FilenameUtils.separatorsToSystem(reluri.toASCIIString());
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("Copied ").append(countCopied);
        msg.append(" [stub jar related ").append(countHits);
        msg.append(", extra files ").append(countExtras);
        msg.append(", resources ").append(countResources);
        msg.append("] - only ").append(javalisting.size()).append(" left to find!");
        return msg.toString();
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

package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;

import net.erdfelt.android.sdkfido.local.JarListing;
import net.erdfelt.android.sdkfido.util.PathUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class SourceCopier {
    private LinkedList<String>  javalisting;
    private FileWriter          logwriter;
    private PrintWriter         out;
    private int                 countCopied;
    private int                 countHits;
    private int                 countExtras;
    private int                 countResources;

    public SourceCopier(Dir logDir) throws IOException {
        this.javalisting = new LinkedList<String>();
        File logfile = logDir.getFile("sdkfido.log");
        this.logwriter = new FileWriter(logfile, false);
        this.out = new PrintWriter(logwriter);
    }

    public void setNarrowSearchTo(JarListing jarlisting) {
        javalisting.addAll(jarlisting.getJavaSourceListing());
    }

    public void identifyCopiedFiles(Dir sourceDir) throws IOException {
        if (javalisting == null) {
            return; // nothing to do here
        }
        File searchFile;
        String javafilename;
        ListIterator<String> iterlisting = javalisting.listIterator();
        while (iterlisting.hasNext()) {
            javafilename = iterlisting.next();
            searchFile = sourceDir.getFile(javafilename);
            if (searchFile.exists()) {
                iterlisting.remove();
                out.println("[FOUND] " + javafilename);
            }
        }
    }

    public void copyTree(File searchDir, Dir sourceDir, Dir resourceDir) throws IOException {
        if (searchDir.exists()) {
            copyDirectory(searchDir, sourceDir, resourceDir);
            identifyCopiedFiles(sourceDir);
        }
    }

    private void copyDirectory(File basedir, Dir sourceDir, Dir resourceDir) throws IOException {
        System.out.printf("Copying Dir: %s%n[", basedir);
        recurseDirCopy(basedir, basedir, sourceDir, resourceDir);
        System.out.println("]");
    }

    private void recurseDirCopy(File basedir, File dir, Dir sourceDir, Dir resourceDir) throws IOException {
        String name, relpath;
        System.out.print(".");

        for (File path : dir.listFiles()) {
            if (path.isDirectory()) {
                recurseDirCopy(basedir, path, sourceDir, resourceDir);
            } else if (path.isFile()) {
                relpath = PathUtil.toRelativePath(basedir, path);
                name = path.getName();
                countCopied++;
                if (name.endsWith(".java")) {
                    if (javalisting.contains(relpath)) {
                        countHits++;
                    } else {
                        countExtras++;
                    }
                    FileUtils.copyFile(path, sourceDir.getFile(relpath));
                } else {
                    countResources++;
                    FileUtils.copyFile(path, resourceDir.getFile(relpath));
                }
            }
        }
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

    public void close() {
        for (String javafilename : javalisting) {
            out.println("[NOT FOUND] " + javafilename);
        }

        IOUtils.closeQuietly(out);
        IOUtils.closeQuietly(logwriter);
    }
}

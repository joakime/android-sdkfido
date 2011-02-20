package net.erdfelt.android.sdkfido.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class AidlCompiler {
    private static final Logger LOG     = Logger.getLogger(AidlCompiler.class.getName());
    private static File         aidlBin = null;
    private OutputProject       project;

    public AidlCompiler(OutputProject project) {
        this.project = project;
    }

    public static void setAidlBin(File aidlBin) {
        AidlCompiler.aidlBin = aidlBin;
    }

    public void compile() throws IOException {
        List<String> aidls = this.project.getResourceDir().findFilePaths("^.*\\.aidl$");
        deleteRedundantAIDL(aidls);

        for (String aidlpath : aidls) {
            compileAIDL(aidlpath);
        }
    }

    private void compileAIDL(String aidlpath) {
        List<String> commands = new ArrayList<String>();
        commands.add(AidlCompiler.aidlBin.getAbsolutePath());
        String relSourceDir = this.project.getBaseDir().getRelativePath(this.project.getSourceDir());
        String relOutputDir = this.project.getBaseDir().getRelativePath(this.project.getOutputDir());
        String relAidlPath = this.project.getBaseDir().getRelativePath(this.project.getSourceDir().getFile(aidlpath));
        commands.add("-o" + relSourceDir);
        commands.add("-I" + relOutputDir);
        commands.add(relAidlPath);

        try {
            ProcessBuilder pid = new ProcessBuilder(commands);
            pid.directory(this.project.getBaseDir().getPath());
            pid.redirectErrorStream(true);
            Process process = pid.start();

            InputStream stream = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Unable to execute aidl", e);
        }
    }

    private void deleteRedundantAIDL(List<String> aidls) throws IOException {
        LOG.info("Remove redundant aidl files ...");
        int count = 0;
        String javapath;
        File javafile;
        for (String aidlpath : aidls) {
            javapath = aidlpath.replaceFirst("\\.aidl$", ".java");
            javafile = this.project.getSourceDir().getFile(javapath);
            if (javafile.exists()) {
                FileUtils.forceDelete(this.project.getResourceDir().getFile(aidlpath));
                count++;
            }
        }
        LOG.info("Removed " + count + " redundant aidl files");
    }
}

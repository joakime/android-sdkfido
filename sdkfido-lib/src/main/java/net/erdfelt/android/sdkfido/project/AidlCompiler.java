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
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class AidlCompiler {
    private static final Logger LOG     = Logger.getLogger(AidlCompiler.class.getName());
    private static File         aidlBin = null;
    private Dir                 baseDir;

    public AidlCompiler(Dir baseDir) {
        this.baseDir = baseDir;
    }

    public static void setAidlBin(File aidlBin) {
        AidlCompiler.aidlBin = aidlBin;
    }

    public void compile(Dir sourceDir, Dir resourceDir, Dir outputDir) throws IOException {
        List<String> aidls = resourceDir.findFilePaths("^.*\\.aidl$");
        // deleteRedundantAIDL(aidls, sourceDir, resourceDir);

        // compileAllAIDLs(aidls, sourceDir, resourceDir, outputDir);

        for (String aidlpath : aidls) {
            compileAIDL(aidlpath, sourceDir, resourceDir, outputDir);
        }
    }

    private void compileAllAIDLs(List<String> aidls, Dir sourceDir, Dir resourceDir, Dir outputDir) {
        List<String> commands = new ArrayList<String>();
        commands.add(AidlCompiler.aidlBin.getAbsolutePath());
        String relSourceDir = baseDir.getRelativePath(sourceDir);
        String relOutputDir = baseDir.getRelativePath(outputDir);
        String relResourceDir = baseDir.getRelativePath(resourceDir);
        commands.add("-o" + relSourceDir);
        commands.add("-I" + relOutputDir + File.pathSeparator + relResourceDir + File.pathSeparator + relSourceDir);
        for (String aidlpath : aidls) {
            commands.add(FilenameUtils.separatorsToSystem(aidlpath));
        }

        try {
            ProcessBuilder pid = new ProcessBuilder(commands);
            pid.directory(baseDir.getPath());
            pid.redirectErrorStream(true);

            LOG.info("Process (in dir: " + pid.directory() + ") - " + StringUtils.join(commands, " "));

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

    private void compileAIDL(String aidlpath, Dir sourceDir, Dir resourceDir, Dir outputDir) {
        List<String> commands = new ArrayList<String>();
        commands.add(AidlCompiler.aidlBin.getAbsolutePath());
        String relSourceDir = baseDir.getRelativePath(sourceDir);
        String relResourceDir = baseDir.getRelativePath(resourceDir);
        String relOutputDir = baseDir.getRelativePath(outputDir);
        String relAidlPath = baseDir.getRelativePath(resourceDir.getFile(aidlpath));
        commands.add("-o" + relSourceDir);
        commands.add("-I" + relOutputDir + File.pathSeparator + relResourceDir + File.pathSeparator + relSourceDir);
        commands.add(relAidlPath);

        try {
            ProcessBuilder pid = new ProcessBuilder(commands);
            pid.directory(baseDir.getPath());
            pid.redirectErrorStream(true);

            LOG.info("Process (in dir: " + pid.directory() + ") - " + StringUtils.join(commands, " "));

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

    private void deleteRedundantAIDL(List<String> aidls, Dir sourceDir, Dir resourceDir) throws IOException {
        LOG.info("Remove redundant aidl files ...");
        int count = 0;
        String javapath;
        File javafile;
        for (String aidlpath : aidls) {
            javapath = aidlpath.replaceFirst("\\.aidl$", ".java");
            javafile = sourceDir.getFile(javapath);
            if (javafile.exists()) {
                FileUtils.forceDelete(resourceDir.getFile(aidlpath));
                count++;
            }
        }
        LOG.info("Removed " + count + " redundant aidl files");
    }
}

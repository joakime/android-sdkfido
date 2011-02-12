package net.erdfelt.android.sdkfido.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.project.Project;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class ProcessAidlFilesTask implements Task {
    private static final Logger LOG = Logger.getLogger(ProcessAidlFilesTask.class.getName());
    private Project             project;
    private File                aidlBin;

    public ProcessAidlFilesTask(LocalAndroidPlatforms platforms, Project project) throws FileNotFoundException {
        this.project = project;
        this.aidlBin = platforms.getBin("aidl");
    }

    @Override
    public String getName() {
        return "Process *.aidl files";
    }

    @Override
    public void run(TaskListener tasklistener, TaskQueue tasks) throws Throwable {
        deleteRedundantAIDL();
        processAIDL();
    }

    private void processAIDL() {
        LOG.info("Process AIDLs");
        List<String> aidls = project.getResourcesPathsOfType(".aidl");
        for (String aidlpath : aidls) {
            compileAIDL(aidlpath);
        }
    }

    private void compileAIDL(String aidlpath) {
        List<String> commands = new ArrayList<String>();
        commands.add(this.aidlBin.getAbsolutePath());
        commands.add("-o" + OS("src/main/java"));
        commands.add("-I" + OS("target/classes"));
        commands.add(OS("src/main/resources/" + aidlpath));

        try {
            ProcessBuilder pid = new ProcessBuilder(commands);
            pid.directory(project.getBaseDir());
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

    private String OS(String path) {
        return FilenameUtils.separatorsToSystem(path);
    }

    private void deleteRedundantAIDL() throws IOException {
        LOG.info("Remove redundant aidl files ...");
        int count = 0;
        String javapath;
        File javafile;
        List<String> aidls = project.getResourcesPathsOfType(".aidl");
        for (String aidlpath : aidls) {
            javapath = aidlpath.replaceFirst("\\.aidl$", ".java");
            javafile = project.getSrcJava(javapath);
            if (javafile.exists()) {
                FileUtils.forceDelete(project.getSrcResource(aidlpath));
                count++;
            }
        }
        LOG.info("Removed " + count + " redundant aidl files");
    }
}

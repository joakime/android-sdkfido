package net.erdfelt.android.sdkfido.project;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class Project {
    private File   baseDir;
    private File   srcJava;
    private File   srcResources;
    private String id;

    public Project(File projectsDir, String id) {
        baseDir = new File(projectsDir, id);
        srcJava = new File(baseDir, FilenameUtils.separatorsToSystem("src/main/java"));
        srcResources = new File(baseDir, FilenameUtils.separatorsToSystem("src/main/resources"));
    }

    public File getSrcJava() {
        return srcJava;
    }

    public File getSrcJava(String path) {
        return new File(srcJava, FilenameUtils.separatorsToSystem(path));
    }

    public File getSrcResources() {
        return srcResources;
    }

    public File getSrcResource(String path) {
        return new File(srcResources, FilenameUtils.separatorsToSystem(path));
    }

    public void create() {
        mkdirs(baseDir);
        mkdirs(srcJava);
        mkdirs(srcResources);
    }

    private void mkdirs(File dir) {
        if (dir.exists()) {
            return;
        }

        dir.mkdirs();
    }

    public File getBaseDir() {
        return baseDir;
    }

    public String getId() {
        return id;
    }
}

package net.erdfelt.android.sdkfido.project;

import java.io.File;

public class Project {
    private File   baseDir;
    private String id;

    public Project(File projectsDir, String id) {
        baseDir = new File(projectsDir, id);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
    }

    public File getBaseDir() {
        return baseDir;
    }

    public String getId() {
        return id;
    }
}

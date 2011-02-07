package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.sdks.AndroidSdks;
import net.erdfelt.android.sdkfido.sdks.AndroidSdksLoader;

public class Fetcher {
    private AndroidSdks sdks;
    private WorkDir     workdir;
    private File        projectsDir;
    private Config      config;

    public Fetcher() throws IOException {
        config = new Config();
        sdks = AndroidSdksLoader.load();
        workdir = new WorkDir(config.getDir("work.dir"));
        projectsDir = config.getDir("projects.dir");
    }

    public AndroidSdks getSdks() {
        return sdks;
    }

    public void setSdks(AndroidSdks sdks) {
        this.sdks = sdks;
    }

    public WorkDir getWorkdir() {
        return workdir;
    }

    public void setWorkdir(WorkDir workdir) {
        this.workdir = workdir;
    }

    public File getProjectsDir() {
        return projectsDir;
    }

    public void setProjectsDir(File projectsDir) {
        this.projectsDir = projectsDir;
    }
}

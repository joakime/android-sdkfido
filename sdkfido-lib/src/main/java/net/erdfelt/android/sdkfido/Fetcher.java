package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.sdks.AndroidSdks;
import net.erdfelt.android.sdkfido.sdks.AndroidSdksLoader;

public class Fetcher {
    private AndroidSdks           sdks;
    private WorkDir               workdir;
    private File                  projectsDir;
    private LocalAndroidPlatforms platforms;
    private Config                config;

    public void setConfig(Config config) throws IOException {
        this.config = config;
        this.sdks = AndroidSdksLoader.load();

        reconfigurePlatformsDir();
        reconfigureWorkDir();
        reconfigureProjectsDir();
    }

    private void reconfigureProjectsDir() {
        File defaultDir = new File(config.getConfigHome(), "projects");
        projectsDir = config.getFile("projects.dir", defaultDir);
    }

    private void reconfigureWorkDir() {
        File defaultDir = new File(config.getConfigHome(), "work");
        File dir = config.getFile("work.dir", defaultDir);
        workdir = new WorkDir(dir);
    }

    public void reconfigurePlatformsDir() throws IOException {
        File platformsDir = config.getFile("platforms.dir");
        if (platformsDir != null) {
            platforms = new LocalAndroidPlatforms(platformsDir);
        } else {
            platforms = LocalAndroidPlatforms.findLocalJavaSdk();
        }
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

    public LocalAndroidPlatforms getPlatforms() {
        return platforms;
    }

    public void setPlatforms(LocalAndroidPlatforms platforms) {
        this.platforms = platforms;
    }

    public Config getConfig() {
        return config;
    }
}

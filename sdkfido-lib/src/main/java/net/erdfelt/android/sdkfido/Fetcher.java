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
    private boolean               generateMavenBuild = true;
    private boolean               generateAntBuild   = false;

    public Config getConfig() {
        return config;
    }

    public LocalAndroidPlatforms getPlatforms() {
        return platforms;
    }

    public File getProjectsDir() {
        return projectsDir;
    }

    public AndroidSdks getSdks() {
        return sdks;
    }

    public WorkDir getWorkdir() {
        return workdir;
    }

    public boolean isGenerateAntBuild() {
        return generateAntBuild;
    }

    public boolean isGenerateMavenBuild() {
        return generateMavenBuild;
    }

    public void reconfigurePlatformsDir() throws IOException {
        File platformsDir = config.getFile("platforms.dir");
        if (platformsDir != null) {
            platforms = new LocalAndroidPlatforms(platformsDir);
        } else {
            platforms = LocalAndroidPlatforms.findLocalJavaSdk();
        }
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

    public void setConfig(Config config) throws IOException {
        this.config = config;
        this.sdks = AndroidSdksLoader.load();

        this.generateMavenBuild = config.getBoolean("generate.build.maven", true);
        this.generateAntBuild = config.getBoolean("generate.build.ant", true);

        reconfigurePlatformsDir();
        reconfigureWorkDir();
        reconfigureProjectsDir();
    }

    public void setGenerateAntBuild(boolean generateAntBuild) {
        this.generateAntBuild = generateAntBuild;
        
        config.setBoolean("generate.build.ant", this.generateAntBuild);
    }

    public void setGenerateMavenBuild(boolean generateMavenBuild) {
        this.generateMavenBuild = generateMavenBuild;
    }

    public void setPlatforms(LocalAndroidPlatforms platforms) {
        this.platforms = platforms;
    }

    public void setProjectsDir(File projectsDir) {
        this.projectsDir = projectsDir;
    }

    public void setSdks(AndroidSdks sdks) {
        this.sdks = sdks;
    }

    public void setWorkdir(WorkDir workdir) {
        this.workdir = workdir;
    }
}

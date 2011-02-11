package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.git.GitFactory;
import net.erdfelt.android.sdkfido.git.GitMirrors;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.project.Project;
import net.erdfelt.android.sdkfido.sdks.AndroidSdks;
import net.erdfelt.android.sdkfido.sdks.AndroidSdksLoader;
import net.erdfelt.android.sdkfido.sdks.Sdk;
import net.erdfelt.android.sdkfido.sdks.SdkRepo;
import net.erdfelt.android.sdkfido.tasks.CopyGitSourceToProjectTask;
import net.erdfelt.android.sdkfido.tasks.GenerateAntBuildTask;
import net.erdfelt.android.sdkfido.tasks.GenerateMavenBuildTask;
import net.erdfelt.android.sdkfido.tasks.GitCloneTask;
import net.erdfelt.android.sdkfido.tasks.GitSwitchBranchTask;
import net.erdfelt.android.sdkfido.tasks.InitProjectTask;

/**
 * Main controller class.
 */
public class Fetcher {
    private AndroidSdks           sdks;
    private WorkDir               workdir;
    private LocalAndroidPlatforms platforms;
    private Config                config;

    public TaskQueue getFetchTasks(Sdk sdk) {
        TaskQueue tasks = new TaskQueue();
        
        File mirrorxml = new File(config.getConfigHome(), "gitmirrors.xml");
        GitMirrors mirrors = GitMirrors.load(mirrorxml);
        GitFactory.setMirrors(mirrors);

        Project project = new Project(config.getProjectsDir(), sdk);
        AndroidPlatform platform = platforms.getPlatform("android-" + sdk.getApilevel());

        for (SdkRepo repo : sdk.getRepos()) {
            tasks.add(new GitCloneTask(workdir, repo));
            tasks.add(new GitSwitchBranchTask(workdir, repo, repo.getBranch()));
            tasks.add(new InitProjectTask(project));
            tasks.add(new CopyGitSourceToProjectTask(workdir, repo, platform, project));
        }

        if (config.getGenerateAntBuild()) {
            tasks.add(new GenerateAntBuildTask(project, sdk));
        }

        if (config.getGenerateMavenBuild()) {
            tasks.add(new GenerateMavenBuildTask(project, sdk));
        }

        return tasks;
    }

    public Config getConfig() {
        return config;
    }

    public LocalAndroidPlatforms getPlatforms() {
        return platforms;
    }

    public AndroidSdks getSdks() {
        return sdks;
    }

    public WorkDir getWorkDir() {
        return workdir;
    }

    public void setConfig(Config config) throws IOException {
        this.config = config;
        this.sdks = AndroidSdksLoader.load();
        this.platforms = new LocalAndroidPlatforms(config.getPlatformsDir());
        this.workdir = new WorkDir(config.getWorkDir());
    }
}

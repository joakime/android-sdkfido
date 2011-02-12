package net.erdfelt.android.sdkfido;

import java.io.IOException;

import net.erdfelt.android.sdkfido.git.GitException;
import net.erdfelt.android.sdkfido.git.GitFactory;
import net.erdfelt.android.sdkfido.git.GitMirrors;
import net.erdfelt.android.sdkfido.git.IGit;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.JarListing;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.project.Project;
import net.erdfelt.android.sdkfido.project.SourceCopier;
import net.erdfelt.android.sdkfido.sdks.AndroidSdks;
import net.erdfelt.android.sdkfido.sdks.AndroidSdksLoader;
import net.erdfelt.android.sdkfido.sdks.Sdk;
import net.erdfelt.android.sdkfido.sdks.SdkRepo;
import net.erdfelt.android.sdkfido.tasks.CloseProjectTask;
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

    public TaskQueue getFetchTasks(Sdk sdk, AndroidPlatform platform) throws IOException, GitException {
        TaskQueue tasks = new TaskQueue();

        GitMirrors mirrors = GitMirrors.load();
        GitFactory.setMirrors(mirrors);

        Project project = new Project(config.getProjectsDir(), sdk);
        project.delete(Project.COPIED_SOURCE_LOG);

        JarListing jarlisting = platform.getAndroidJarListing();
        SourceCopier copier = new SourceCopier(jarlisting.getJavaSourceListing());
        copier.setProject(project);
        
        tasks.add(new InitProjectTask(project, copier));
        for (SdkRepo repo : sdk.getRepos()) {
            IGit git = workdir.getGitRepo(repo.getUrl());

            tasks.add(new GitCloneTask(git, repo.getUrl()));
            tasks.add(new GitSwitchBranchTask(git, repo.getBranch()));
            tasks.add(new CopyGitSourceToProjectTask(git, repo, copier));
        }
        tasks.add(new CloseProjectTask(copier));

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

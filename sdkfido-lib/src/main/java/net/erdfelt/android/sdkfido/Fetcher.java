package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.git.GitException;
import net.erdfelt.android.sdkfido.git.GitFactory;
import net.erdfelt.android.sdkfido.git.GitMirrors;
import net.erdfelt.android.sdkfido.git.IGit;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.JarListing;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.project.AidlCompiler;
import net.erdfelt.android.sdkfido.project.AntOutputProject;
import net.erdfelt.android.sdkfido.project.MavenMultimoduleOutputProject;
import net.erdfelt.android.sdkfido.project.MavenOutputProject;
import net.erdfelt.android.sdkfido.project.OutputProject;
import net.erdfelt.android.sdkfido.project.SdkOutputProject;
import net.erdfelt.android.sdkfido.sdks.Repo;
import net.erdfelt.android.sdkfido.sdks.SourceOrigins;
import net.erdfelt.android.sdkfido.sdks.SourceOriginsLoader;
import net.erdfelt.android.sdkfido.tasks.GitCloneTask;
import net.erdfelt.android.sdkfido.tasks.GitSwitchBranchTask;
import net.erdfelt.android.sdkfido.tasks.ProjectCloseTask;
import net.erdfelt.android.sdkfido.tasks.ProjectCopySourceTask;
import net.erdfelt.android.sdkfido.tasks.ProjectInitTask;
import net.erdfelt.android.sdkfido.tasks.ProjectValidateApiTask;

/**
 * Main controller class.
 */
public class Fetcher {
    private WorkDir               workdir;
    private LocalAndroidPlatforms platforms;
    private FetcherConfig         config;
    private SourceOrigins         origins;

    public TaskQueue getFetchTasks(FetchTarget target) throws IOException, GitException {
        TaskQueue tasks = new TaskQueue();

        GitMirrors mirrors = GitMirrors.load();
        GitFactory.setMirrors(mirrors);
        
        OutputProject project = null;
        switch (config.getOutputType()) {
            case MAVEN_BUILD:
                project = new MavenOutputProject(config.getOutputDir(), target);
                break;
            case MAVEN_BUILD_MULTI:
                project = new MavenMultimoduleOutputProject(config.getOutputDir(), target);
                break;
            case SDK_SOURCE:
                project = new SdkOutputProject(getPlatforms(), target);
                break;
            case ANT_BUILD:
            default:
                project = new AntOutputProject(config.getOutputDir(), target);
                break;
        }

        tasks.add(new ProjectInitTask(project));

        for (Repo repo : getSourceOrigins().getRepos()) {
            IGit git = workdir.getGitRepo(repo.getUrl());

            tasks.add(new GitCloneTask(git, repo.getUrl()));
            tasks.add(new GitSwitchBranchTask(git, target.getBranchname()));
            tasks.add(new ProjectCopySourceTask(git, repo, project));
        }
        
        if( (platforms != null) && (platforms.valid())) {
            AidlCompiler.setAidlBin(platforms.getBin("aidl"));
            project.setEnableAidlCompilation(true);

            if (target.getApilevel() != null) {
                AndroidPlatform platform = platforms.getPlatform(target.getApilevel());
                if(platform != null) {
                    File stubFile = platform.getAndroidJarFile();
                    project.setAndroidStub(target.getApilevel(), stubFile);
                }
            }
        }

        tasks.add(new ProjectCloseTask(project));

        return tasks;
    }

    public FetcherConfig getConfig() {
        return config;
    }

    public SourceOrigins getSourceOrigins() {
        return origins;
    }

    public void setSourceOrigins(SourceOrigins origins) {
        this.origins = origins;
    }

    public LocalAndroidPlatforms getPlatforms() {
        return platforms;
    }

    public WorkDir getWorkDir() {
        return workdir;
    }

    public void setConfig(FetcherConfig config) throws IOException {
        this.config = config;
        this.platforms = new LocalAndroidPlatforms(config.getPlatformsDir());
        this.workdir = new WorkDir(config.getWorkDir());
        if (this.origins == null) {
            this.origins = SourceOriginsLoader.load();
        }
    }
}

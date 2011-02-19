package net.erdfelt.android.sdkfido;

import java.io.IOException;

import net.erdfelt.android.sdkfido.git.GitException;
import net.erdfelt.android.sdkfido.git.GitFactory;
import net.erdfelt.android.sdkfido.git.GitMirrors;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.sdks.SourceOrigins;
import net.erdfelt.android.sdkfido.sdks.SourceOriginsLoader;

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

        // Project project = new Project(config.getOutputDir(), sdk);
        // project.delete(Project.COPIED_SOURCE_LOG);

        // JarListing jarlisting = platform.getAndroidJarListing();
        // SourceCopier copier = new SourceCopier(jarlisting.getJavaSourceListing());
        // copier.setProject(project);

        // tasks.add(new InitProjectTask(platform, project, copier));
        // for (SdkRepo repo : sdk.getRepos()) {
        // IGit git = workdir.getGitRepo(repo.getUrl());

        // tasks.add(new GitCloneTask(git, repo.getUrl()));
        // tasks.add(new GitSwitchBranchTask(git, repo.getBranch()));
        // tasks.add(new CopyGitSourceToProjectTask(git, repo, copier));
        // }
        // tasks.add(new ProcessAidlFilesTask(platforms, project));
        // tasks.add(new ValidateJavaPackagesTask(project));
        // tasks.add(new GenerateBuildFilesTask(project, sdk));
        // tasks.add(new CloseProjectTask(copier));

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

    public FetchTarget getFetchTarget(String targetName) {
        // TODO Auto-generated method stub
        return null;
    }
}

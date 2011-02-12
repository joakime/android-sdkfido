package net.erdfelt.android.sdkfido.tasks;

import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.git.IGit;
import net.erdfelt.android.sdkfido.project.SourceCopier;
import net.erdfelt.android.sdkfido.sdks.SdkRepo;

public class CopyGitSourceToProjectTask implements Task {
    private static final Logger LOG = Logger.getLogger(CopyGitSourceToProjectTask.class.getName());
    private IGit                git;
    private SdkRepo             repo;
    private SourceCopier        copier;

    public CopyGitSourceToProjectTask(IGit git, SdkRepo repo, SourceCopier copier) {
        super();
        this.git = git;
        this.repo = repo;
        this.copier = copier;
    }

    public IGit getGit() {
        return git;
    }

    public void setGit(IGit git) {
        this.git = git;
    }

    public SourceCopier getCopier() {
        return copier;
    }

    public void setCopier(SourceCopier copier) {
        this.copier = copier;
    }

    public SdkRepo getRepo() {
        return repo;
    }

    public void setRepo(SdkRepo repo) {
        this.repo = repo;
    }

    @Override
    public String getName() {
        return "Copy Sources";
    }

    @Override
    public void run(TaskListener listener, TaskQueue tasks) throws Throwable {
        for (String include : repo.getIncludes()) {
            copier.searchTree(git.getDir(), include);
        }
        LOG.info("Copier Results: " + copier);
    }
}

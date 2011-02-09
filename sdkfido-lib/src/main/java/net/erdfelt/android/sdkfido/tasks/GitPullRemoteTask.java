package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.WorkDir;
import net.erdfelt.android.sdkfido.git.GitRepo;
import net.erdfelt.android.sdkfido.sdks.SdkRepo;

public class GitPullRemoteTask implements Task {
    private WorkDir workdir;
    private SdkRepo repo;

    public GitPullRemoteTask(WorkDir workdir, SdkRepo repo) {
        this.workdir = workdir;
        this.repo = repo;
    }

    @Override
    public String getName() {
        return "Git Pull Remote: " + repo.getUrl();
    }

    @Override
    public void run(TaskListener listener, TaskQueue tasks) throws Throwable {
        GitRepo git = workdir.getGitRepo(repo.getUrl());
        git.pullRemote("origin");
    }
}

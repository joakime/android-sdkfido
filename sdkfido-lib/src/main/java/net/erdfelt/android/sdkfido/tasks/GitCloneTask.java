package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.WorkDir;
import net.erdfelt.android.sdkfido.git.GitRepo;
import net.erdfelt.android.sdkfido.sdks.SdkRepo;

public class GitCloneTask implements Task {
    private WorkDir workdir;
    private SdkRepo repo;

    public GitCloneTask(WorkDir workdir, SdkRepo repo) {
        this.workdir = workdir;
        this.repo = repo;
    }

    @Override
    public String getName() {
        return "Git Clone: " + repo.getUrl();
    }

    @Override
    public void run(TaskListener listener, TaskQueue tasks) throws Throwable {
        GitRepo git = workdir.getGitRepo(repo.getUrl());

        if (git.exists()) {
            tasks.insertAtHead(new GitUpdateTask(workdir, repo));
            return;
        }

        git.createBareRepo();
        String remoteName = "origin";
        git.addRemoteConfig(remoteName, repo.getUrl());
        git.fetch(remoteName);
    }
}

package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.WorkDir;
import net.erdfelt.android.sdkfido.git.IGit;
import net.erdfelt.android.sdkfido.sdks.SdkRepo;

public class GitSwitchBranchTask implements Task {
    private WorkDir workdir;
    private SdkRepo repo;
    private String  branch;

    public GitSwitchBranchTask(WorkDir workdir, SdkRepo repo, String branch) {
        this.workdir = workdir;
        this.repo = repo;
        this.branch = branch;
    }

    @Override
    public String getName() {
        return "Git Switch Branch: " + branch;
    }

    @Override
    public void run(TaskListener listener, TaskQueue tasks) throws Throwable {
        IGit git = workdir.getGitRepo(repo.getUrl());
        git.checkoutBranch(branch);
    }
}

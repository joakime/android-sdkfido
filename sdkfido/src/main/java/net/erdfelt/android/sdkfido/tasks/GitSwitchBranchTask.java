package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.git.IGit;

import org.apache.commons.lang.StringUtils;

public class GitSwitchBranchTask implements Task {
    private IGit   git;
    private String branch;

    public GitSwitchBranchTask(IGit git, String branch) {
        this.git = git;
        this.branch = branch;
        if (StringUtils.isBlank(branch)) {
            throw new IllegalArgumentException("Branch Name cannot be blank");
        }
    }

    @Override
    public String getName() {
        return "Git Switch Branch: " + branch;
    }

    @Override
    public void run(TaskQueue tasks) throws Throwable {
        git.checkoutBranch(branch);
    }
}

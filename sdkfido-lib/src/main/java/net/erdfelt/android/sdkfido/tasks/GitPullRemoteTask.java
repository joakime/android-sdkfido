package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.git.IGit;

public class GitPullRemoteTask implements Task {
    private IGit git;

    public GitPullRemoteTask(IGit git) {
        this.git = git;
    }

    @Override
    public String getName() {
        return "Git Pull Remote: " + IGit.REMOTE_NAME;
    }

    @Override
    public void run(TaskQueue tasks) throws Throwable {
        git.pullRemote();
    }
}

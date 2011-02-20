package net.erdfelt.android.sdkfido.tasks;

import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.git.IGit;

public class GitCloneTask implements Task {
    private static final Logger LOG = Logger.getLogger(GitCloneTask.class.getName());
    private IGit git;
    private String remoteUrl;

    public GitCloneTask(IGit git, String url) {
        this.git = git;
        this.remoteUrl = url;
    }

    @Override
    public String getName() {
        return "Git Clone: " + remoteUrl;
    }

    @Override
    public void run(TaskQueue tasks) throws Throwable {
        if (git.exists()) {
            LOG.info("git clone already performed, issuing pull instead.");
            tasks.insertAtHead(new GitPullRemoteTask(git));
            return;
        }

        git.clone(remoteUrl);
    }
}

package net.erdfelt.android.sdkfido.tasks;

import static org.hamcrest.Matchers.*;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.WorkDir;
import net.erdfelt.android.sdkfido.git.GitFactory;
import net.erdfelt.android.sdkfido.git.GitMirrors;
import net.erdfelt.android.sdkfido.git.IGit;
import net.erdfelt.android.sdkfido.logging.Logging;
import net.erdfelt.android.sdkfido.sdks.Repo;

import org.eclipse.jetty.toolchain.test.TestingDir;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class GitCloneTaskTest {
    @Rule
    public TestingDir testdir = new TestingDir();

    static {
        Logging.config();
    }

    @Test
    public void testCloneAndBranchLauncher() throws Throwable {
        testdir.ensureEmpty();

        GitMirrors mirrors = GitMirrors.load();
        GitFactory.setMirrors(mirrors);

        WorkDir workdir = new WorkDir(testdir.getFile("work"));

        Repo repo = new Repo();
        repo.setUrl("git://android.git.kernel.org/platform/packages/apps/Launcher.git");
        String branchName = "android-sdk-2.0.1_r1";

        IGit git = workdir.getGitRepo(repo.getUrl());

        GitCloneTask gitclone = new GitCloneTask(git, repo.getUrl());
        GitSwitchBranchTask gitbranch = new GitSwitchBranchTask(git, branchName);

        TaskQueue tasks = new TaskQueue();

        gitclone.run(tasks);
        gitbranch.run(tasks);

        Assert.assertThat("Branch name", git.getCurrentBranch(), is("refs/tags/" + branchName));
    }
}

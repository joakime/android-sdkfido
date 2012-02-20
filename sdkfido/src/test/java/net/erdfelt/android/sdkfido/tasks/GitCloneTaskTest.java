/*******************************************************************************
 *    Copyright 2012 - Joakim Erdfelt
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
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

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
package net.erdfelt.android.sdkfido.git.internal;

import static org.hamcrest.Matchers.*;

import java.io.File;

import net.erdfelt.android.sdkfido.git.GitMirrors;
import net.erdfelt.android.sdkfido.git.internal.InternalGit;
import net.erdfelt.android.sdkfido.git.internal.TerseProgressMonitor;
import net.erdfelt.android.sdkfido.logging.Logging;

import org.eclipse.jetty.toolchain.test.FS;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class InternalGitTest {
    @Rule
    public TestingDir testdir = new TestingDir();

    static {
        Logging.config();
    }

    @Test
    public void testClone() throws Throwable {
        testdir.ensureEmpty();

        File repodir = testdir.getFile("repo");
        FS.ensureDirExists(repodir);

        InternalGit repo = new InternalGit(repodir, new GitMirrors());
        Assert.assertThat("repo.exist()", repo.exists(), is(false));

        String url = "http://joakim.erdfelt.com/git/clone-empty-jar.git";
        repo.setMonitor(new TerseProgressMonitor());
        repo.clone(url);

        Assert.assertThat("repo.getCurrentBranch()", repo.getCurrentBranch(), is("refs/heads/master"));
    }

    @Test
    public void testCloneLarge() throws Throwable {
        testdir.ensureEmpty();

        File repodir = testdir.getFile("repo");
        FS.ensureDirExists(repodir);

        InternalGit repo = new InternalGit(repodir, new GitMirrors());
        Assert.assertThat("repo.exist()", repo.exists(), is(false));

        String url = "http://joakim.erdfelt.com/git/large.git";
        repo.setMonitor(new TerseProgressMonitor());
        repo.clone(url);

        Assert.assertThat("repo.getCurrentBranch()", repo.getCurrentBranch(), is("refs/heads/master"));
    }
}

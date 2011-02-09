package net.erdfelt.android.sdkfido.git;

import static org.hamcrest.Matchers.*;

import java.io.File;

import net.erdfelt.android.sdkfido.logging.Logging;

import org.eclipse.jetty.toolchain.test.FS;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class GitRepoTest {
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

        GitRepo repo = new GitRepo(repodir);
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

        GitRepo repo = new GitRepo(repodir);
        Assert.assertThat("repo.exist()", repo.exists(), is(false));

        String url = "http://joakim.erdfelt.com/git/large.git";
        repo.setMonitor(new TerseProgressMonitor());
        repo.clone(url);

        Assert.assertThat("repo.getCurrentBranch()", repo.getCurrentBranch(), is("refs/heads/master"));
    }
}

package net.erdfelt.android.sdkfido.git;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class GitMirrorsTest {
    @Test
    public void testLoadMirrors() throws IOException, SAXException {
        File mirrorxml = MavenTestingUtils.getTestResourceFile("config-tests/gitmirrors.xml");
        GitMirrors mirrors = GitMirrors.load(mirrorxml);
        Assert.assertThat("mirrors", mirrors, notNullValue());
        Assert.assertThat("mirrors.size", mirrors.size(), is(3));

        String originalUrl = "git://android.git.kernel.org/platform/packages/apps/Launcher.git";
        String expected = "http://internal.repos.local/android/Launcher.git";
        String actual = mirrors.getNewUrl(originalUrl);

        Assert.assertThat("New URL", actual, is(expected));
    }

    @Test
    public void testLoadNonexistantMirrors() throws IOException, SAXException {
        File badmirrorxml = new File(MavenTestingUtils.getTestResourcesDir(), "badmirror.xml");
        GitMirrors mirrors = GitMirrors.load(badmirrorxml);
        Assert.assertThat("mirrors", mirrors, notNullValue());
        Assert.assertThat("mirrors.size", mirrors.size(), is(0));

        String originalUrl = "git://android.git.kernel.org/platform/packages/apps/Launcher.git";
        String expected = originalUrl;
        String actual = mirrors.getNewUrl(originalUrl);

        Assert.assertThat("New URL", actual, is(expected));
    }
}

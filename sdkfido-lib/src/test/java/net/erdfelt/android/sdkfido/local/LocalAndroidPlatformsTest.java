package net.erdfelt.android.sdkfido.local;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.erdfelt.android.sdkfido.logging.Logging;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Assert;
import org.junit.Test;

public class LocalAndroidPlatformsTest {
    static {
        Logging.config();
    }

    /**
     * Doesn't assert anything, just used to exercise the code.
     */
    @Test
    public void testFindLocalJavaSdk() {
        try {
            File dir = LocalAndroidPlatforms.findLocalJavaSdk();
            System.out.println("Local Android SDK Dir = " + dir);
            if (dir != null) {
                LocalAndroidPlatforms platforms = new LocalAndroidPlatforms(dir);
                System.out.println("LocalAndroidPlatforms = " + platforms);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPlatformSdk06() throws IOException {
        File sdkDir = MavenTestingUtils.getTestResourceDir("dummy-local-java-sdks/sdk_r06");
        LocalAndroidPlatforms platforms = new LocalAndroidPlatforms(sdkDir);

        Assert.assertThat("Platforms.exists()", platforms.exists(), is(true));
        Assert.assertThat("Platforms.getSdkRelease()", platforms.getSdkRelease(), is(6));

        Assert.assertThat("Platforms.size", platforms.size(), is(4));

        AndroidPlatform plat4 = platforms.getPlatform("android-4");
        Assert.assertThat("Platform[android-4]", plat4, notNullValue());
        Assert.assertThat("Platform[android-4]", plat4.getApiLevel(), is(4));
        Assert.assertThat("Platform[android-4]", plat4.getDescription(), is("Android SDK Platform 1.6_r2"));
        Assert.assertThat("Platform[android-4]", plat4.getVersion(), is("1.6"));

        JarListing jar4 = plat4.getAndroidJarListing();
        Assert.assertThat("Platform[android-4].androidJarListing", jar4, notNullValue());
        Assert.assertThat("Platform[android-4].androidJarListing.size", jar4.size(), is(3509));
        List<String> classlist = jar4.getClassList();
        Assert.assertThat("Platform[android-4].androidJarListing.classlist.size", classlist.size(), is(2400));
    }
}

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
package net.erdfelt.android.sdkfido.local;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.erdfelt.android.sdkfido.logging.Logging;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.PathAssert;
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
            e.printStackTrace(); // Intentionally swallow exceptions, don't want JUnit to know about em.
        }
    }

    @Test
    public void testPlatformSdk06() throws Exception {
        File sdkDir = MavenTestingUtils.getTestResourceDir("dummy-local-java-sdks/sdk_r06");
        LocalAndroidPlatforms platforms = new LocalAndroidPlatforms(sdkDir);

        Assert.assertThat("Platforms.exists()", platforms.exists(), is(true));
        Assert.assertThat("Platforms.getSdkRelease()", platforms.getSdkRelease(), is(6));

        Assert.assertThat("Platforms.size", platforms.size(), is(4));

        Assert.assertThat("Platforms.hasApiLevel(4)", platforms.hasApiLevel("4"), is(true));

        assertIsTestPlatform4("Platform[android-4]", sdkDir, platforms.getPlatform("android-4")); // Long Form
        assertIsTestPlatform4("Platform[4]", sdkDir, platforms.getPlatform("4")); // Short (Api Only) Form
    }

    private void assertIsTestPlatform4(String prefix, File sdkDir, AndroidPlatform plat4) throws IOException {
        Assert.assertThat(prefix, plat4, notNullValue());
        File expectedDir = new File(sdkDir, FilenameUtils.separatorsToSystem("platforms/android-4"));
        PathAssert.assertDirExists("Expected Testing Platform Dir", expectedDir);
        Assert.assertThat(prefix + ".dir", plat4.getDir(), is(expectedDir));
        Assert.assertThat(prefix + ".apiLevel", plat4.getApiLevel(), is(4));
        Assert.assertThat(prefix + ".description", plat4.getDescription(), is("Android SDK Platform 1.6_r2"));
        Assert.assertThat(prefix + ".version", plat4.getVersion(), is("1.6"));

        JarListing jar4 = plat4.getAndroidJarListing();
        Assert.assertThat(prefix + ".androidJarListing", jar4, notNullValue());
        Assert.assertThat(prefix + ".androidJarListing.size", jar4.size(), is(3509));
        List<String> classlist = jar4.getClassList();
        Assert.assertThat(prefix + ".androidJarListing.classlist.size", classlist.size(), is(2400));
    }
}

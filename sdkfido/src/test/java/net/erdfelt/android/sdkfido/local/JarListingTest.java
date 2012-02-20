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

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Assert;
import org.junit.Test;

public class JarListingTest {
    @Test
    public void testSimpleListing() throws IOException {
        File testjar = MavenTestingUtils.getTestResourceFile("android-4-empty.jar");
        JarListing listing = new JarListing(testjar);
        assertAndroidApi4Jar(listing);
    }

    /**
     * Test the fetch of a jar listing using the alternate technique of a raw listing sitting next to the jar file with
     * the "${filename_of_jarfile}.listing"
     */
    @Test
    public void testAltListing() throws IOException {
        File testjar = MavenTestingUtils
                .getTestResourceFile("dummy-local-java-sdks/sdk_r06/platforms/android-4/android.jar");
        JarListing listing = new JarListing(testjar);
        assertAndroidApi4Jar(listing);
    }

    private void assertAndroidApi4Jar(JarListing listing) {
        Assert.assertThat("Listing.size", listing.size(), is(3509));

        List<String> classlist = listing.getClassList();
        Assert.assertThat("ClassList.size", classlist.size(), is(2400));
        Assert.assertThat("ClassList", classlist, hasItem("android/os/Build.class"));
    }
}

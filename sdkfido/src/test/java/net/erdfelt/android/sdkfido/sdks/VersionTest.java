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
package net.erdfelt.android.sdkfido.sdks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class VersionTest {
    @Test
    public void testConstructorString() {
        assertVersion(new Version("1"), 1, 0, 0);
        assertVersion(new Version("1.0"), 1, 0, 0);
        assertVersion(new Version("1.0.0"), 1, 0, 0);

        assertVersion(new Version("2"), 2, 0, 0);
        assertVersion(new Version("2.1"), 2, 1, 0);
        assertVersion(new Version("2.3.1"), 2, 3, 1);
    }

    @Test
    public void testVersionComparable() {
        String unsorted[] = { "1.5", "2.3.1", "1.0", "2.2", "2.3" };
        String expected[] = { "2.3.1", "2.3", "2.2", "1.5", "1.0" };

        List<Version> versions = new ArrayList<Version>();
        for (String u : unsorted) {
            versions.add(new Version(u));
        }

        System.out.printf("Unsorted: %s%n", versions);
        Collections.sort(versions);
        System.out.printf("Sorted: %s%n", versions);

        int len = expected.length;
        Assert.assertEquals("Expected.length", expected.length, versions.size());
        for (int i = 0; i < len; i++) {
            Assert.assertEquals("Expected[" + i + "]", expected[i], versions.get(i).toString());
        }
    }

    private void assertVersion(Version ver, int expectedMajor, int expectedMinor, int expectedPoint) {
        Assert.assertEquals("Version[" + ver + "].major", expectedMajor, ver.getMajor());
        Assert.assertEquals("Version[" + ver + "].minor", expectedMinor, ver.getMinor());
        Assert.assertEquals("Version[" + ver + "].point", expectedPoint, ver.getPoint());
    }
}

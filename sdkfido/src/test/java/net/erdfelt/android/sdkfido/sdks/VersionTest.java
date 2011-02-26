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

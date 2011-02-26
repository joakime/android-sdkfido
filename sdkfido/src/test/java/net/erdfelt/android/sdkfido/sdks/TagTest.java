package net.erdfelt.android.sdkfido.sdks;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class TagTest {
    @Test
    public void testSort() {
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag("android-sdk-1.5-pre", "1.5"));
        tags.add(new Tag("android-sdk-1.6_r2", "1.6"));
        tags.add(new Tag("android-sdk-2.0.1_r1", "2.0.1"));
        tags.add(new Tag("android-sdk-2.2_r1", "2.2"));
        tags.add(new Tag("android-sdk-1.5_r1", "1.5"));
        tags.add(new Tag("android-sdk-2.0_r1", "2.0"));
        tags.add(new Tag("android-sdk-2.1_r1", "2.1"));
        tags.add(new Tag("android-sdk-1.5_r3", "1.5"));
        tags.add(new Tag("android-sdk-1.6_r1", "1.6"));
        tags.add(new Tag("android-sdk-2.2_r2", "2.2"));

        String expectedOrder[] = { "android-sdk-2.2_r2", "android-sdk-2.2_r1", "android-sdk-2.1_r1",
                "android-sdk-2.0.1_r1", "android-sdk-2.0_r1", "android-sdk-1.6_r2", "android-sdk-1.6_r1",
                "android-sdk-1.5_r3", "android-sdk-1.5_r1", "android-sdk-1.5-pre" };

        assertSortOrder(expectedOrder, tags);
    }

    private void assertSortOrder(String[] expectedOrder, List<Tag> tags) {
        Assert.assertEquals("unsorted and expected order length", expectedOrder.length, tags.size());

        System.out.printf("Original order: %s%n", order(tags));
        Collections.sort(tags);
        System.out.printf("Sorted order: %s%n", order(tags));

        int len = expectedOrder.length;
        for (int i = 0; i < len; i++) {
            Tag api = tags.get(i);
            Assert.assertThat("tags[" + i + "].name", api.getName(), is(expectedOrder[i]));
        }
    }

    private String order(List<Tag> tags) {
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");
        boolean delim = false;
        for (Tag tag : tags) {
            if (delim) {
                buf.append(", ");
            }
            buf.append(tag.getName());
            delim = true;
        }
        buf.append(" }");
        return buf.toString();
    }
}

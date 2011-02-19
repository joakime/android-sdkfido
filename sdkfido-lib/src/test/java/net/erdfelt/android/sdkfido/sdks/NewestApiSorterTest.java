package net.erdfelt.android.sdkfido.sdks;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class NewestApiSorterTest {
    @Test
    public void testSortBasic() {
        String unsortedOrder[] = { "10", "5", "4", "9", "11" };
        String expectedOrder[] = { "11", "10", "9", "5", "4" };

        assertSortOrder(expectedOrder, unsortedOrder);
    }

    @Test
    public void testSortComplex() {
        String unsortedOrder[] = { "Honeycomb","10", "", null, "5", "4", "9", "11" };
        String expectedOrder[] = { "11", "10", "9", "5", "4", "Honeycomb", "", null };

        assertSortOrder(expectedOrder, unsortedOrder);
    }

    private void assertSortOrder(String[] expectedOrder, String[] unsortedOrder) {
        Assert.assertEquals("unsorted and expected order length", expectedOrder.length, unsortedOrder.length);

        List<ApiLevel> apis = new ArrayList<ApiLevel>();
        for (String unsorted : unsortedOrder) {
            apis.add(new ApiLevel(unsorted));
        }

        System.out.printf("Original order: %s%n", order(apis));
        Collections.sort(apis, NewestApiSorter.INSTANCE);
        System.out.printf("Sorted order: %s%n", order(apis));

        int len = expectedOrder.length;
        Assert.assertThat("apis.length", apis.size(), is(len));
        for (int i = 0; i < len; i++) {
            ApiLevel api = apis.get(i);
            Assert.assertThat("apis[" + i + "].level", api.getLevel(), is(expectedOrder[i]));
        }
    }

    private String order(List<ApiLevel> apis) {
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");
        boolean delim = false;
        for (ApiLevel api : apis) {
            if (delim) {
                buf.append(", ");
            }
            buf.append(api.getLevel());
            delim = true;
        }
        buf.append(" }");
        return buf.toString();
    }
}

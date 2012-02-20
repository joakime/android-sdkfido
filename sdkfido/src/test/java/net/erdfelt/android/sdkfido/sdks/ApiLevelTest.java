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

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ApiLevelTest {
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
        Collections.sort(apis);
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

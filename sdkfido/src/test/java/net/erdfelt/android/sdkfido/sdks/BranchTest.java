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

public class BranchTest {

    @Test
    public void testSortBasic() {
        List<Branch> unsorted = new ArrayList<Branch>();
        unsorted.add(new Branch("eclair-release", "2.0"));
        unsorted.add(new Branch("donut-release2", "1.6"));
        unsorted.add(new Branch("gingerbread-release", "2.3"));
        unsorted.add(new Branch("cupcake-release", "1.5"));
        unsorted.add(new Branch("release-1.0", "1.0"));
        unsorted.add(new Branch("froyo-release", "2.2"));
        unsorted.add(new Branch("donut-release", "1.6"));

        String expectedOrder[] = { "gingerbread-release", "froyo-release", "eclair-release", "donut-release2",
                "donut-release", "cupcake-release", "release-1.0" };

        assertSortOrder(expectedOrder, unsorted);
    }

    private void assertSortOrder(String[] expectedOrder, List<Branch> branches) {
        Assert.assertEquals("unsorted and expected order length", expectedOrder.length, branches.size());

        System.out.printf("Original order: %s%n", order(branches));
        Collections.sort(branches);
        System.out.printf("Sorted order: %s%n", order(branches));

        int len = expectedOrder.length;
        for (int i = 0; i < len; i++) {
            Branch branch = branches.get(i);
            Assert.assertThat("branches[" + i + "].name", branch.getName(), is(expectedOrder[i]));
        }
    }

    private String order(List<Branch> branches) {
        StringBuilder buf = new StringBuilder();
        buf.append("{ ");
        boolean delim = false;
        for (Branch branch : branches) {
            if (delim) {
                buf.append(", ");
            }
            buf.append(branch.getName());
            delim = true;
        }
        buf.append(" }");
        return buf.toString();
    }

}

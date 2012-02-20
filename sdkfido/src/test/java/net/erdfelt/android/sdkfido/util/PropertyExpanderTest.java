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
package net.erdfelt.android.sdkfido.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class PropertyExpanderTest {
    @Test
    public void testExpand() {
        Map<String,String> props = new HashMap<String, String>();
        props.put("MOVIE", "Amelie");
        
        PropertyExpander expander = new PropertyExpander(props);
        String expected = "My favorite movie is Amelie";
        String actual = expander.expand("My favorite movie is @MOVIE@");
        Assert.assertEquals(expected, actual);
    }
}

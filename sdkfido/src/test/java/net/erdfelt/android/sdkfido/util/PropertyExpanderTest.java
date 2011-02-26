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

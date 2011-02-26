package net.erdfelt.android.sdkfido.configer;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

public class ConfigurableComparator implements Comparator<Configurable> {
    private Collator collator = Collator.getInstance();

    public int compare(Configurable c1, Configurable c2) {
        CollationKey key1 = asKey(c1);
        CollationKey key2 = asKey(c2);
        return key1.compareTo(key2);
    }

    private CollationKey asKey(Configurable c) {
        if (c == null) {
            return collator.getCollationKey("");
        }
        String key = c.getKey();
        if (StringUtils.isBlank(key)) {
            return collator.getCollationKey("");
        }
        return collator.getCollationKey(key.trim());
    }
}

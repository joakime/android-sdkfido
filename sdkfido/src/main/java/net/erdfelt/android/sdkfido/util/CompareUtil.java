package net.erdfelt.android.sdkfido.util;

import java.text.CollationKey;
import java.text.Collator;

import org.apache.commons.lang.StringUtils;

public final class CompareUtil {
    private static Collator collator = Collator.getInstance();

    public static int compare(String str1, String str2) {
        CollationKey key1 = asKey(str1);
        CollationKey key2 = asKey(str2);
        return key1.compareTo(key2);
    }

    private static CollationKey asKey(String str) {
        return collator.getCollationKey(StringUtils.defaultString(str));
    }
}

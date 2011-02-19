package net.erdfelt.android.sdkfido.sdks;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

import net.erdfelt.android.sdkfido.FetchTarget;

public class FetchTargetComparator implements Comparator<FetchTarget> {
    public static final FetchTargetComparator INSTANCE = new FetchTargetComparator();
    private Collator                          collator = Collator.getInstance();

    @Override
    public int compare(FetchTarget o1, FetchTarget o2) {
        CollationKey key1 = asKey(o1);
        CollationKey key2 = asKey(o2);

        return key1.compareTo(key2);
    }

    private CollationKey asKey(FetchTarget target) {
        if (target == null) {
            return emptyKey();
        }
        StringBuilder key = new StringBuilder();
        if (target.getType() == null) {
            key.append("");
        } else {
            key.append(target.getType().name());
        }
        key.append("|").append(target.getId());
        return collator.getCollationKey(key.toString());
    }

    private CollationKey emptyKey() {
        return collator.getCollationKey("");
    }
}

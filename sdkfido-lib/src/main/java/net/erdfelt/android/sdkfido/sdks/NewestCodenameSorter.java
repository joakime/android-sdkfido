package net.erdfelt.android.sdkfido.sdks;

import java.util.Comparator;

public class NewestCodenameSorter implements Comparator<String> {
    public static final NewestCodenameSorter INSTANCE = new NewestCodenameSorter();

    @Override
    public int compare(String o1, String o2) {
        if ((o1 == null) && (o2 == null)) {
            return 0;
        }
        if (o2 == null) {
            return -1;
        }
        if (o1 == null) {
            return 1;
        }
        return o2.compareTo(o1);
    }
}

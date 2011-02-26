package net.erdfelt.android.sdkfido.sdks;

import java.util.Comparator;

public class VersionComparator implements Comparator<Version> {
    public static final VersionComparator INSTANCE = new VersionComparator();

    @Override
    public int compare(Version o1, Version o2) {
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

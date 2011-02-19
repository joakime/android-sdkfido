package net.erdfelt.android.sdkfido.sdks;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

public class NewestApiSorter implements Comparator<ApiLevel> {
    public static final NewestApiSorter INSTANCE = new NewestApiSorter();

    @Override
    public int compare(ApiLevel o1, ApiLevel o2) {
        if ((o1 == null) && (o2 == null)) {
            return 0;
        }
        if (unset(o2)) {
            return -1;
        }
        if (unset(o1)) {
            return 1;
        }

        int level1 = Integer.parseInt(o1.getLevel());
        int level2 = Integer.parseInt(o2.getLevel());
        return level2 - level1;
    }

    private boolean unset(ApiLevel api) {
        if (api == null) {
            return true;
        }
        if (StringUtils.isBlank(api.getLevel())) {
            return true;
        }
        if (!StringUtils.isNumeric(api.getLevel())) {
            return true;
        }
        return false;
    }
}

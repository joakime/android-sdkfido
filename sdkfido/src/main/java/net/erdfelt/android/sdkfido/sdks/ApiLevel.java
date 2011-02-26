package net.erdfelt.android.sdkfido.sdks;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;
import org.apache.commons.lang.StringUtils;

@ObjectCreate(pattern = "android-source/apilevels/api")
public class ApiLevel implements Comparable<ApiLevel> {
    @SetProperty(pattern = "android-source/apilevels/api")
    private String  level;
    @SetProperty(pattern = "android-source/apilevels/api")
    private Version version;
    @SetProperty(pattern = "android-source/apilevels/api")
    private String  codename;

    public ApiLevel() {
        super();
    }

    public ApiLevel(String level) {
        super();
        this.level = level;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ApiLevel other = (ApiLevel) obj;
        if (level == null) {
            if (other.level != null) {
                return false;
            }
        } else if (!level.equals(other.level)) {
            return false;
        }
        return true;
    }

    public String getCodename() {
        return codename;
    }

    public String getLevel() {
        return level;
    }

    public Version getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        return result;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public int compareTo(ApiLevel o) {
        if ((o == null) || (StringUtils.isBlank(o.level)) || (!StringUtils.isNumeric(o.level))) {
            return -1;
        }
        if ((StringUtils.isBlank(this.level) || (!StringUtils.isNumeric(this.level)))) {
            return 1;
        }
        int l1 = Integer.parseInt(this.level);
        int l2 = Integer.parseInt(o.level);
        return l2 - l1;
    }
}

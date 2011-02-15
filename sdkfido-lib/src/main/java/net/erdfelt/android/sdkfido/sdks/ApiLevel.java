package net.erdfelt.android.sdkfido.sdks;

import net.erdfelt.android.sdkfido.util.CompareUtil;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/apilevels/api")
public class ApiLevel implements Comparable<ApiLevel>{
    @SetProperty(pattern = "android-source/apilevels/api")
    private String level;
    @SetProperty(pattern = "android-source/apilevels/api")
    private String version;
    @SetProperty(pattern = "android-source/apilevels/api")
    private String codename;

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

    public String getVersion() {
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

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int compareTo(ApiLevel o) {
        return CompareUtil.compare(this.level, o.level);
    }
}

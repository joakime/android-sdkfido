package net.erdfelt.android.sdkfido.sdks;

import net.erdfelt.android.sdkfido.util.CompareUtil;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/tags/tag")
public class Tag implements Comparable<Tag> {
    @SetProperty(pattern = "android-source/tags/tag")
    private String name;
    @SetProperty(pattern = "android-source/tags/tag")
    private String version;

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
        Tag other = (Tag) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int compareTo(Tag o) {
        int diff = CompareUtil.compare(this.version, o.version);
        if (diff != 0) {
            return diff;
        }
        return CompareUtil.compare(this.name, o.name);
    }
}

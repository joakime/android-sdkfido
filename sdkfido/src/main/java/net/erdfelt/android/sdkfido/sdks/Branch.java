package net.erdfelt.android.sdkfido.sdks;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/branches/branch")
public class Branch implements Comparable<Branch> {
    @SetProperty(pattern = "android-source/branches/branch")
    private String  name;
    @SetProperty(pattern = "android-source/branches/branch")
    private Version version;

    public Branch() {
        super();
    }

    public Branch(String name, String version) {
        super();
        this.name = name;
        this.version = new Version(version);
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
        Branch other = (Branch) obj;
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

    public Version getVersion() {
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

    public void setVersion(Version version) {
        this.version = version;
    }

    public void setVersion(String version) {
        this.version = new Version(version);
    }

    @Override
    public int compareTo(Branch o) {
        // Default order for version is reverse
        int diff = this.version.compareTo(o.version);
        if (diff != 0) {
            return diff;
        }
        // Reverse compare on String object
        return o.name.compareTo(this.name);
    }
}

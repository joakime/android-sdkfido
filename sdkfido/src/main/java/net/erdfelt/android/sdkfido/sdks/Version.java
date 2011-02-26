package net.erdfelt.android.sdkfido.sdks;

import org.apache.commons.lang.StringUtils;

/**
 * Represents the version identification string.
 */
public class Version implements Comparable<Version> {
    private int major;
    private int minor;
    private int point;

    public Version(int maj, int minor) {
        this(maj, minor, 0);
    }

    public Version(int maj, int minor, int point) {
        this.major = maj;
        this.minor = minor;
        this.point = 0;
    }

    public Version(String ver) {
        String parts[] = StringUtils.split(ver, ".");
        if (parts.length > 3) {
            throw new IllegalArgumentException("Invalid version identifier string [" + ver
                    + "] it can only have max 3 parts");
        }
        this.major = Integer.parseInt(parts[0]);
        if (parts.length > 1) {
            this.minor = Integer.parseInt(parts[1]);
            if (parts.length > 2) {
                this.point = Integer.parseInt(parts[2]);
            }
        }
    }

    @Override
    public int compareTo(Version o) {
        int diff = o.major - this.major;
        if (diff != 0) {
            return diff;
        }

        diff = o.minor - this.minor;
        if (diff != 0) {
            return diff;
        }

        return o.point - this.point;
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
        Version other = (Version) obj;
        if (major != other.major) {
            return false;
        }
        if (minor != other.minor) {
            return false;
        }
        if (point != other.point) {
            return false;
        }
        return true;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + major;
        result = prime * result + minor;
        result = prime * result + point;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append(this.major).append('.').append(this.minor);
        if (this.point > 0) {
            msg.append('.').append(this.point);
        }
        return msg.toString();
    }
}

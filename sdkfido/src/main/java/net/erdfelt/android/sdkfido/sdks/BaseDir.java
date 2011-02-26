package net.erdfelt.android.sdkfido.sdks;

import net.erdfelt.android.sdkfido.util.CompareUtil;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/repos/repo/basedir")
public class BaseDir implements Comparable<BaseDir> {
    @SetProperty(pattern = "android-source/repos/repo/basedir")
    private String path;
    @SetProperty(pattern = "android-source/repos/repo/basedir")
    private String project;

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
        BaseDir other = (BaseDir) obj;
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        return true;
    }

    public String getPath() {
        return path;
    }

    public String getProject() {
        return project;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setProject(String project) {
        this.project = project;
    }

    @Override
    public int compareTo(BaseDir o) {
        return CompareUtil.compare(this.path, o.path);
    }
}

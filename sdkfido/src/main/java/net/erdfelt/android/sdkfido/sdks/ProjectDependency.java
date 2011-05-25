package net.erdfelt.android.sdkfido.sdks;

import net.erdfelt.android.sdkfido.util.CompareUtil;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/projects/project/dependency")
public class ProjectDependency implements Comparable<ProjectDependency> {
    @SetProperty(pattern = "android-source/projects/project/dependency")
    private String          ref;
    private ProjectTemplate project;

    @Override
    public int compareTo(ProjectDependency other) {
        return CompareUtil.compare(this.ref, other.ref);
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
        ProjectDependency other = (ProjectDependency) obj;
        if (ref == null) {
            if (other.ref != null) {
                return false;
            }
        } else if (!ref.equals(other.ref)) {
            return false;
        }
        return true;
    }

    public String getRef() {
        return ref;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ref == null) ? 0 : ref.hashCode());
        return result;
    }

    public ProjectTemplate getProject() {
        return project;
    }

    public void setProject(ProjectTemplate project) {
        this.project = project;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

}

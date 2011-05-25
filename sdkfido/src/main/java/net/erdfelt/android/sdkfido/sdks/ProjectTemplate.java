package net.erdfelt.android.sdkfido.sdks;

import java.util.Set;
import java.util.TreeSet;

import net.erdfelt.android.sdkfido.project.maven.MavenConstants;
import net.erdfelt.android.sdkfido.util.CompareUtil;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

/**
 * Represents a Project Template
 */
@ObjectCreate(pattern = "android-source/projects/project")
public class ProjectTemplate implements Comparable<ProjectTemplate> {
    @SetProperty(pattern = "android-source/projects/project")
    private String                 id;
    @SetProperty(pattern = "android-source/projects/project", attributeName="template")
    private String                 templateName;
    @SetProperty(pattern = "android-source/projects/project")
    private String                 groupId = MavenConstants.DEFAULT_GROUPID;
    @SetProperty(pattern = "android-source/projects/project")
    private String                 parentGroupId;
    @SetProperty(pattern = "android-source/projects/project")
    private String                 parentArtifactId;
    private String                 parentVersion;
    private String                 version;
    private String                 apiLevel;
    private Set<ProjectDependency> dependencies = new TreeSet<ProjectDependency>();

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return id;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public String getParentArtifactId() {
        return parentArtifactId;
    }

    public void setParentArtifactId(String parentArtifactId) {
        this.parentArtifactId = parentArtifactId;
    }

    public String getParentVersion() {
        return parentVersion;
    }

    public void setParentVersion(String parentVersion) {
        this.parentVersion = parentVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(String apiLevel) {
        this.apiLevel = apiLevel;
    }

    @SetNext
    public void addDependency(ProjectDependency dep) {
        this.dependencies.add(dep);
    }

    @Override
    public int compareTo(ProjectTemplate other) {
        int diff = 0;

        diff = CompareUtil.compare(this.id, other.id);
        if (diff != 0) {
            return diff;
        }

        diff = CompareUtil.compare(this.templateName, other.templateName);
        return diff;
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
        ProjectTemplate other = (ProjectTemplate) obj;
        if (dependencies == null) {
            if (other.dependencies != null) {
                return false;
            }
        } else if (!dependencies.equals(other.dependencies)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (templateName == null) {
            if (other.templateName != null) {
                return false;
            }
        } else if (!templateName.equals(other.templateName)) {
            return false;
        }
        return true;
    }

    public Set<ProjectDependency> getDependencies() {
        return dependencies;
    }

    public String getId() {
        return id;
    }

    public String getTemplateName() {
        return templateName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dependencies == null) ? 0 : dependencies.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
        return result;
    }

    public void setDependencies(Set<ProjectDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public boolean hasDependencies() {
        return (dependencies != null) && (dependencies.isEmpty() == false);
    }
}

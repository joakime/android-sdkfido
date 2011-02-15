package net.erdfelt.android.sdkfido.sdks;

import java.util.Set;
import java.util.TreeSet;

public class Version {
    private String      version;
    private Set<String> tags      = new TreeSet<String>();
    private Set<String> branches  = new TreeSet<String>();
    private Set<String> apis      = new TreeSet<String>();
    private Set<String> codenames = new TreeSet<String>();
    
    public void addTag(String tag) {
        this.tags.add(tag);
    }
    
    public void addBranch(String branch) {
        this.branches.add(branch);
    }
    
    public void addApi(String api) {
        this.apis.add(api);
    }
    
    public void addCodename(String codename) {
        this.codenames.add(codename);
    }

    public Set<String> getCodenames() {
        return codenames;
    }

    public void setCodenames(Set<String> codenames) {
        this.codenames = codenames;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getBranches() {
        return branches;
    }

    public void setBranches(Set<String> branches) {
        this.branches = branches;
    }

    public Set<String> getApis() {
        return apis;
    }

    public void setApis(Set<String> apis) {
        this.apis = apis;
    }
}

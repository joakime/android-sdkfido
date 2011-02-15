package net.erdfelt.android.sdkfido.sdks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;
import org.apache.commons.lang.StringUtils;

@ObjectCreate(pattern = "android-source")
public class SourceOrigins {
    @SetProperty(pattern = "android-source", attributeName = "spec-version")
    private int                  specVersion;

    private Set<ApiLevel>        apilevels = new TreeSet<ApiLevel>();
    private Set<Tag>             tags      = new TreeSet<Tag>();
    private Set<Branch>          branches  = new TreeSet<Branch>();
    private Set<Repo>            repos     = new TreeSet<Repo>();
    private Map<String, Version> versions  = new HashMap<String, Version>();

    public Map<String, Version> getVersions() {
        return versions;
    }

    public void setVersions(Map<String, Version> versions) {
        this.versions = versions;
    }

    public int getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(int specVersion) {
        this.specVersion = specVersion;
    }

    public Set<ApiLevel> getApilevels() {
        return apilevels;
    }

    public void setApilevels(Set<ApiLevel> apilevels) {
        this.apilevels = apilevels;
    }

    @SetNext
    public void addApiLevel(ApiLevel apilevel) {
        this.apilevels.add(apilevel);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @SetNext
    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public Set<Branch> getBranches() {
        return branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    @SetNext
    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public Set<Repo> getRepos() {
        return repos;
    }

    public void setRepos(Set<Repo> repos) {
        this.repos = repos;
    }

    @SetNext
    public void addRepo(Repo repo) {
        this.repos.add(repo);
    }

    /**
     * Get the {@link ApiLevel} by level id
     * 
     * @param level
     *            the level id
     * @return the ApiLevel found
     */
    public ApiLevel getApiLevel(String level) throws SourceOriginNotFoundException {
        for (ApiLevel api : apilevels) {
            if (StringUtils.equals(api.getLevel(), level)) {
                return api;
            }
        }
        throw new SourceOriginNotFoundException("Unable to find ApiLevel of: " + level);
    }

    /**
     * Get the {@link Tag} by name
     * 
     * @param name
     *            the name of the tag
     * @return the Tag found
     */
    public Tag getTag(String name) throws SourceOriginNotFoundException {
        for (Tag tag : tags) {
            if (StringUtils.equals(tag.getName(), name)) {
                return tag;
            }
        }
        throw new SourceOriginNotFoundException("Unable to find tag with specified name: " + name);
    }

    /**
     * Get a {@link Branch} by name.
     * 
     * @param name
     *            the name of the branch
     * @return the Branch found
     * @throws SourceOriginNotFoundException
     *             if unable to find the branch
     */
    public Branch getBranch(String name) throws SourceOriginNotFoundException {
        for (Branch branch : branches) {
            if (StringUtils.equals(branch.getName(), name)) {
                return branch;
            }
        }
        throw new SourceOriginNotFoundException("Unable to find branch with specified name: " + name);
    }

    /**
     * Get a repo reference by its URL
     * 
     * @param url
     *            url to the repo
     * @return the repo details.
     * @throws SourceOriginNotFoundException
     *             if unable to find a repo with the specified url
     */
    public Repo getRepo(String url) throws SourceOriginNotFoundException {
        for (Repo repo : repos) {
            if (StringUtils.equals(repo.getUrl(), url)) {
                return repo;
            }
        }
        throw new SourceOriginNotFoundException("Unable to find repository with specified url: " + url);
    }

    /**
     * Connect the various versions discovered together.
     */
    public void normalize() {
        Version version;

        for (ApiLevel api : apilevels) {
            version = getVersion(api.getVersion(), true);
            version.addApi(api.getLevel());
            version.addCodename(api.getCodename());
            setVersion(version);
        }

        for (Tag tag : tags) {
            version = getVersion(tag.getVersion(), true);
            version.addTag(tag.getName());
            setVersion(version);
        }

        for (Branch branch : branches) {
            version = getVersion(branch.getVersion(), true);
            version.addBranch(branch.getName());
            setVersion(version);
        }
    }

    private void setVersion(Version version) {
        versions.put(version.getVersion(), version);
    }

    public Version getVersion(String version) {
        return versions.get(version);
    }

    private Version getVersion(String version, boolean create) {
        Version v = versions.get(version);
        if ((v == null) && (create)) {
            v = new Version();
            v.setVersion(version);
            versions.put(version, v);
        }
        return v;
    }
}

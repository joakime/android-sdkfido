package net.erdfelt.android.sdkfido.sdks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.erdfelt.android.sdkfido.FetchTarget;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;
import org.apache.commons.lang.StringUtils;

@ObjectCreate(pattern = "android-source")
public class SourceOrigins {
    @SetProperty(pattern = "android-source", attributeName = "spec-version")
    private int                          specVersion;

    private TreeSet<ApiLevel>            apilevels = new TreeSet<ApiLevel>();
    private Set<Tag>                     tags      = new TreeSet<Tag>();
    private Set<Branch>                  branches  = new TreeSet<Branch>();
    private Set<Repo>                    repos     = new TreeSet<Repo>();
    private Map<Version, VersionTree>    versions  = new TreeMap<Version, VersionTree>(VersionComparator.INSTANCE);
    private List<FetchTarget>            targets   = new ArrayList<FetchTarget>();
    private Map<String, ProjectTemplate> projects  = new HashMap<String, ProjectTemplate>();

    public Map<Version, VersionTree> getVersions() {
        return versions;
    }

    public void setVersions(Map<Version, VersionTree> versions) {
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

    public Collection<ProjectTemplate> getProjects() {
        return projects.values();
    }

    public void setApilevels(Set<ApiLevel> apis) {
        this.apilevels.clear();
        this.apilevels.addAll(apis);
    }

    @SetNext
    public void addProject(ProjectTemplate project) {
        this.projects.put(project.getId(), project);
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
        normalizeVersions();
        normalizeTargets();
    }

    private void normalizeTargets() {
        targets.clear();
        String id, apilevel, codename, branchname;
        Version version;

        for (ApiLevel api : apilevels) {
            apilevel = api.getLevel();
            codename = api.getCodename();
            version = api.getVersion();

            VersionTree v = versions.get(version);
            Tag tag = v.getTopTag();
            if (tag != null) {
                branchname = tag.getName();
            } else {
                branchname = null;
            }

            // As APILEVEL
            id = api.getLevel();
            targets.add(new FetchTarget(SourceType.APILEVEL, id, apilevel, codename, version, branchname));
        }

        for (ApiLevel api : apilevels) {
            id = api.getCodename();

            if (StringUtils.isBlank(id)) {
                continue; // Not a valid FetchTarget (no id to fetch via)
            }

            apilevel = api.getLevel();
            codename = api.getCodename();
            version = api.getVersion();

            VersionTree v = versions.get(version);
            Tag tag = v.getTopTag();
            if (tag != null) {
                branchname = tag.getName();
            } else {
                branchname = null;
            }

            // As CODENAME
            targets.add(new FetchTarget(SourceType.CODENAME, id, apilevel, codename, version, branchname));
        }

        for (ApiLevel api : apilevels) {
            apilevel = api.getLevel();
            codename = api.getCodename();
            version = api.getVersion();

            VersionTree v = versions.get(version);
            Tag tag = v.getTopTag();
            if (tag != null) {
                branchname = tag.getName();
            } else {
                branchname = null;
            }

            // As VERSION
            id = api.getVersion().toString();
            targets.add(new FetchTarget(SourceType.VERSION, id, apilevel, codename, version, branchname));
        }

        // As TAG
        for (Tag tag : tags) {
            id = tag.getName();
            version = tag.getVersion();
            branchname = tag.getName();

            ApiLevel api = getApiLevelByVersion(version);
            if (api != null) {
                apilevel = api.getLevel();
                codename = api.getCodename();
            } else {
                apilevel = null;
                codename = null;
            }
            targets.add(new FetchTarget(SourceType.TAG, id, apilevel, codename, version, branchname));
        }

        // As BRANCH
        for (Branch branch : branches) {
            id = branch.getName();
            version = branch.getVersion();
            branchname = branch.getName();

            VersionTree v = versions.get(version);
            ApiLevel api = v.getTopApiLevel();
            if (api != null) {
                apilevel = api.getLevel();
                codename = api.getCodename();
            } else {
                apilevel = null;
                codename = null;
            }

            targets.add(new FetchTarget(SourceType.BRANCH, id, apilevel, codename, version, branchname));
        }
    }

    private void normalizeVersions() {
        versions.clear();

        VersionTree version;

        for (ApiLevel api : apilevels) {
            version = getVersion(api.getVersion(), true);
            version.addApi(api);
            version.addCodename(api.getCodename());
            setVersion(version);
        }

        for (Tag tag : tags) {
            version = getVersion(tag.getVersion(), true);
            version.addTag(tag);
            setVersion(version);
        }

        for (Branch branch : branches) {
            version = getVersion(branch.getVersion(), true);
            version.addBranch(branch);
            setVersion(version);
        }
    }

    public ApiLevel getApiLevelByVersion(Version version) {
        ApiLevel last = null;

        Iterator<ApiLevel> reviter = apilevels.descendingIterator();
        int diff;
        while (reviter.hasNext()) {
            ApiLevel api = reviter.next();
            diff = api.getVersion().compareTo(version);
            if (diff > 0) {
                // Too small
                last = api;
                continue; // Skip
            } else if (diff == 0) {
                // Exact match
                return api;
            } else {
                // Too large
                return last;
            }
        }
        return last;
    }

    private void setVersion(VersionTree version) {
        versions.put(version.getVersion(), version);
    }

    public VersionTree getVersion(String version) {
        return versions.get(version);
    }

    private VersionTree getVersion(Version version, boolean create) {
        VersionTree v = versions.get(version);
        if ((v == null) && (create)) {
            v = new VersionTree();
            v.setVersion(version);
            versions.put(version, v);
        }
        return v;
    }

    public List<FetchTarget> getFetchTargets() {
        return targets;
    }

    public FetchTarget getFetchTarget(String id) {
        for (FetchTarget target : targets) {
            if (target.getId().equals(id)) {
                return target;
            }
        }
        return null;
    }

    public ProjectTemplate getProjectTemplate(String id) {
        return projects.get(id);
    }
}

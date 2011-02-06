package net.erdfelt.android.sdkfido.plan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.CallMethod;
import org.apache.commons.digester.annotations.rules.CallParam;
import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/version/tag")
public class SdkTagRef {
    @SetProperty(pattern = "android-source/version/tag", attributeName = "repo-id")
    private String       repoId;

    @SetProperty(pattern = "android-source/version/tag", attributeName = "name")
    private String       name;

    private List<String> includes = new ArrayList<String>();

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    @CallMethod(pattern = "android-source/version/tag/include")
    public void addInclude(@CallParam(pattern = "android-source/version/tag/include") String include) {
        this.includes.add(include);
    }
}

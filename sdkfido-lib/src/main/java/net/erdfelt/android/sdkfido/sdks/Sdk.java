package net.erdfelt.android.sdkfido.sdks;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.CallMethod;
import org.apache.commons.digester.annotations.rules.CallParam;
import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/sdk")
public class Sdk {
    @SetProperty(pattern = "android-source/sdk", attributeName = "id")
    private String        id;

    @SetProperty(pattern = "android-source/sdk", attributeName = "version")
    private String        version;

    @SetProperty(pattern = "android-source/sdk", attributeName = "codename")
    private String        codename;

    @SetProperty(pattern = "android-source/sdk", attributeName = "apilevel")
    private int           apilevel;

    private List<SdkRepo> repos    = new ArrayList<SdkRepo>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public int getApilevel() {
        return apilevel;
    }

    public void setApilevel(int apilevel) {
        this.apilevel = apilevel;
    }

    public List<SdkRepo> getRepos() {
        return repos;
    }

    public void setRepos(List<SdkRepo> repos) {
        this.repos = repos;
    }

    @SetNext
    public void addRepo(SdkRepo repo) {
        this.repos.add(repo);
    }
}

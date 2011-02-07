package net.erdfelt.android.sdkfido.sdks;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.CallMethod;
import org.apache.commons.digester.annotations.rules.CallParam;
import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/sdk/repo")
public class SdkRepo {
    @SetProperty(pattern = "android-source/sdk/repo")
    private String url;

    @SetProperty(pattern = "android-source/sdk/repo")
    private String branch;
    
    private List<String>  includes = new ArrayList<String>();

    public String getBranch() {
        return branch;
    }

    public String getUrl() {
        return url;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    @CallMethod(pattern = "android-source/sdk/repo/include")
    public void addInclude(@CallParam(pattern = "android-source/sdk/repo/include") String include) {
        this.includes.add(include);
    }
}

package net.erdfelt.android.sdkfido.sdks;

import java.util.ArrayList;
import java.util.List;

public class SdkRepo {
    private String url;
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

    public void addInclude(String include) {
        this.includes.add(include);
    }
}

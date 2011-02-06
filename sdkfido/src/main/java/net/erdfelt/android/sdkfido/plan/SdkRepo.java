package net.erdfelt.android.sdkfido.plan;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/repo")
public class SdkRepo {
    @SetProperty(pattern = "android-source/repo")
    private String id;

    @SetProperty(pattern = "android-source/repo")
    private String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

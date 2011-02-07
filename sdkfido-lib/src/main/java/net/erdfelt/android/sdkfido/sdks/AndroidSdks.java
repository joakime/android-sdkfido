package net.erdfelt.android.sdkfido.sdks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source")
public class AndroidSdks implements Iterable<Sdk> {
    @SetProperty(pattern = "android-source", attributeName = "spec-version")
    private int                 specVersion = 0;
    private Map<String, String> props       = new HashMap<String, String>();
    /**
     * Represents the collection of <code>&lt;sdk&gt;</code> entries, with the sdk id being the key.
     */
    private Map<String, Sdk>    sdkMap      = new HashMap<String, Sdk>();

    public int getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(int specVersion) {
        this.specVersion = specVersion;
    }

    public Map<String, Sdk> getSdkMap() {
        return sdkMap;
    }

    public void setSdkMap(Map<String, Sdk> sdkMap) {
        this.sdkMap = sdkMap;
    }

    @SetNext
    public void addSdkOrigin(Sdk sdk) {
        sdkMap.put(sdk.getId(), sdk);
    }

    public Collection<Sdk> getSdks() {
        return sdkMap.values();
    }

    public Integer getCount() {
        return sdkMap.size();
    }

    @Override
    public Iterator<Sdk> iterator() {
        return sdkMap.values().iterator();
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public Sdk getSdkByVersion(String version) {
        for (Sdk sdk : sdkMap.values()) {
            if (version.equals(sdk.getVersion())) {
                return sdk;
            }
        }
        return null;
    }
}

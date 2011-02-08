package net.erdfelt.android.sdkfido.sdks;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This represents the available SDKs as prepared by the <code>sdks.xml</code> file present within the jar file.
 * <p>
 * Note: This does not represent the  
 */
public class AndroidSdks implements Iterable<Sdk> {
    private int                 specVersion = 0;
    private Map<String, String> props       = new HashMap<String, String>();
    /**
     * Represents the collection of <code>&lt;sdk&gt;</code> entries, with the sdk id being the key.
     */
    private Map<String, Sdk>    sdkMap      = new HashMap<String, Sdk>();

    public void addSdk(Sdk sdk) {
        sdkMap.put(sdk.getId(), sdk);
    }

    public Integer getCount() {
        return sdkMap.size();
    }

    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(this.props);
    }

    public String getProperty(String key) {
        return this.props.get(key);
    }

    public Map<String, String> getProps() {
        return props;
    }

    public Sdk getSdkByVersion(String version) {
        for (Sdk sdk : sdkMap.values()) {
            if (version.equals(sdk.getVersion())) {
                return sdk;
            }
        }
        return null;
    }

    public Map<String, Sdk> getSdkMap() {
        return sdkMap;
    }

    public Collection<Sdk> getSdks() {
        return sdkMap.values();
    }

    public int getSpecVersion() {
        return specVersion;
    }

    @Override
    public Iterator<Sdk> iterator() {
        return sdkMap.values().iterator();
    }

    public void setProperty(String key, String value) {
        this.props.put(key, value);
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public void setSdkMap(Map<String, Sdk> sdkMap) {
        this.sdkMap = sdkMap;
    }

    public void setSpecVersion(int specVersion) {
        this.specVersion = specVersion;
    }
}

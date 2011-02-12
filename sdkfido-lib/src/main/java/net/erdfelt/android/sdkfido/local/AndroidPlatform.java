package net.erdfelt.android.sdkfido.local;

import java.io.File;
import java.io.IOException;

/**
 * Represents a single installed platform present on the locally installed android java sdk.
 */
public class AndroidPlatform {
    private String id;
    private int    apiLevel;
    private String description;
    private String version;
    private File   androidJarFile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(int apiLevel) {
        this.apiLevel = apiLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public File getAndroidJarFile() {
        return androidJarFile;
    }

    public void setAndroidJarFile(File androidJarFile) {
        this.androidJarFile = androidJarFile;
    }

    public JarListing getAndroidJarListing() throws IOException {
        return new JarListing(androidJarFile);
    }
}

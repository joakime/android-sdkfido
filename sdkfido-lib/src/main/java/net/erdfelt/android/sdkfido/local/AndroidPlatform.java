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
    private File   dir;
    private File   androidJarFile;

    public File getAndroidJarFile() {
        return androidJarFile;
    }

    public JarListing getAndroidJarListing() throws IOException {
        return new JarListing(androidJarFile);
    }

    public int getApiLevel() {
        return apiLevel;
    }

    public String getDescription() {
        return description;
    }

    public File getDir() {
        return dir;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public void setAndroidJarFile(File androidJarFile) {
        this.androidJarFile = androidJarFile;
    }

    public void setApiLevel(int apiLevel) {
        this.apiLevel = apiLevel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

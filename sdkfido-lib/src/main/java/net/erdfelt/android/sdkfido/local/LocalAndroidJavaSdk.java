package net.erdfelt.android.sdkfido.local;

import java.io.File;
import java.util.Map;

import net.erdfelt.android.sdkfido.sdks.AndroidSdks;

/**
 * This represents the locally installed Android Java SDK as downloaded from <a
 * href="http://developer.android.com/sdk/index.html">developer.android.com</a>
 * <p>
 * For the android sdk plan of android source repositories, see {@link AndroidSdks}
 */
public class LocalAndroidJavaSdk {
    public static LocalAndroidJavaSdk findLocalJavaSdk() {
        // TODO:
        return null;
    }

    private File homeDir;

    public LocalAndroidJavaSdk(File dir) {
        this.homeDir = dir;
    }

    public boolean exists() {
        return homeDir.exists();
    }

    public InstalledSdk getInstalledSdk(String id) {
        // TODO:
        return null;
    }

    public Map<String, InstalledSdk> getInstalledSdks() {
        // TODO:
        return null;
    }
}

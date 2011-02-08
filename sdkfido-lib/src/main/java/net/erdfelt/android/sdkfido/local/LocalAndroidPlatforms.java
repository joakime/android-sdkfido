package net.erdfelt.android.sdkfido.local;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.sdks.AndroidSdks;

import org.apache.commons.io.IOUtils;

/**
 * This represents the locally installed Android Java SDK as downloaded from <a
 * href="http://developer.android.com/sdk/index.html">developer.android.com</a>
 * <p>
 * For the android sdk plan of android source repositories, see {@link AndroidSdks}
 */
public class LocalAndroidPlatforms {
    private static final Logger LOG = Logger.getLogger(LocalAndroidPlatforms.class.getName());

    public static LocalAndroidPlatforms findLocalJavaSdk() {
        // TODO:
        return null;
    }

    private File                         homeDir;
    private int                          sdkRelease;
    private Map<String, AndroidPlatform> platforms;

    public LocalAndroidPlatforms(File dir) throws IOException {
        this.homeDir = dir;
        this.platforms = new HashMap<String, AndroidPlatform>();
        if (!this.homeDir.exists()) {
            LOG.warning("Directory does not exist: " + this.homeDir);
            return;
        }

        loadToolsDir();
        loadPlatformsDir();
    }

    public void addPlatform(AndroidPlatform platform) {
        if (platform == null) {
            return;
        }
        this.platforms.put(platform.getId(), platform);
    }

    public boolean exists() {
        return homeDir.exists();
    }

    public AndroidPlatform getPlatform(String id) {
        return platforms.get(id);
    }

    public Collection<AndroidPlatform> getPlatforms() {
        return platforms.values();
    }

    public int getSdkRelease() {
        return this.sdkRelease;
    }

    private final AndroidPlatform loadPlatform(File subdir) throws IOException {
        File androidJar = new File(subdir, "android.jar");
        if (!androidJar.exists()) {
            return null;
        }

        AndroidPlatform platform = new AndroidPlatform();
        platform.setAndroidJarFile(androidJar);
        platform.setId(subdir.getName());

        File sourcePropFile = new File(subdir, "source.properties");
        if (!sourcePropFile.exists()) {
            LOG.warning("File does not exist: " + sourcePropFile);
            return platform;
        }

        Properties props = loadProperties(sourcePropFile);
        platform.setApiLevel(toInt(props.getProperty("AndroidVersion.ApiLevel")));
        platform.setDescription(props.getProperty("Pkg.Desc"));
        platform.setVersion(props.getProperty("Platform.Version"));
        return platform;
    }

    private void loadPlatformsDir() throws IOException {
        File platformsDir = new File(this.homeDir, "platforms");
        if (!platformsDir.exists()) {
            LOG.warning("Directory does not exist: " + platformsDir);
            return;
        }

        File subdirs[] = platformsDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File path) {
                return path.isDirectory();
            }
        });

        for (File subdir : subdirs) {
            AndroidPlatform platform = loadPlatform(subdir);
            addPlatform(platform);
        }
    }

    private final Properties loadProperties(File propFile) throws IOException {
        FileReader reader = null;
        try {
            reader = new FileReader(propFile);
            Properties props = new Properties();
            props.load(reader);
            return props;
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private final void loadToolsDir() throws IOException {
        File toolsDir = new File(this.homeDir, "tools");
        if (!toolsDir.exists()) {
            LOG.warning("Directory does not exist: " + toolsDir);
            return;
        }

        File sourcePropFile = new File(toolsDir, "source.properties");
        if (!sourcePropFile.exists()) {
            LOG.warning("File does not exist: " + sourcePropFile);
            return;
        }

        Properties props = loadProperties(sourcePropFile);
        this.sdkRelease = toInt(props.getProperty("Pkg.Revision"));
    }

    public int size() {
        return platforms.size();
    }

    private final int toInt(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            LOG.warning("Unable to parse \"" + raw + "\" as integer");
            return -1;
        }
    }
}

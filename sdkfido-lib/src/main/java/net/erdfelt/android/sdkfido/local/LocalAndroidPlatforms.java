package net.erdfelt.android.sdkfido.local;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * This represents the locally installed Android Java SDK as downloaded from <a
 * href="http://developer.android.com/sdk/index.html">developer.android.com</a>
 * <p>
 * For the android sdk plan of android source repositories, see {@link AndroidSdks}
 */
public class LocalAndroidPlatforms {
    private static final Logger LOG = Logger.getLogger(LocalAndroidPlatforms.class.getName());

    /**
     * Attempt to find the local java sdk using the most common environment variables.
     * 
     * @return the local android java sdk directory
     * @throws IOException
     *             if unable to load the default local java sdk
     */
    public static File findLocalJavaSdk() throws IOException {
        StringBuilder err = new StringBuilder();
        err.append("Unable to find the Local Android Java SDK Folder.");

        // Check Environment Variables First
        String envKeys[] = { "ANDROID_HOME", "ANDROID_SDK_ROOT" };
        for (String envKey : envKeys) {
            File sdkHome = getEnvironmentVariableDir(err, envKey);
            if (sdkHome == null) {
                continue; // skip, not found on that key
            }
            LocalAndroidPlatforms platforms = new LocalAndroidPlatforms(sdkHome);
            if (platforms.valid()) {
                return sdkHome;
            }
        }

        // Check Path for possible android.exe (or similar)
        List<String> searchBins = new ArrayList<String>();
        if (SystemUtils.IS_OS_WINDOWS) {
            searchBins.add("adb.exe");
            searchBins.add("emulator.exe");
            searchBins.add("android.exe");
        } else {
            searchBins.add("adb");
            searchBins.add("emulator");
            searchBins.add("android");
        }

        String pathParts[] = StringUtils.split(System.getenv("PATH"), File.pathSeparatorChar);
        for (String searchBin : searchBins) {
            err.append("\nSearched PATH for ").append(searchBin);
            for (String pathPart : pathParts) {
                File pathDir = new File(pathPart);
                LOG.fine("Searching Path: " + pathDir);
                File bin = new File(pathDir, searchBin);
                if (bin.exists() && bin.isFile() && bin.canExecute()) {
                    File homeDir = bin.getParentFile().getParentFile();
                    LOG.fine("Possible Home Dir: " + homeDir);
                    LocalAndroidPlatforms platforms = new LocalAndroidPlatforms(homeDir);
                    if (platforms.valid) {
                        return homeDir;
                    }
                }
            }
            err.append(", not found.");
        }

        throw new FileNotFoundException(err.toString());
    }

    private static File getEnvironmentVariableDir(StringBuilder err, String key) {
        err.append("\nThe environment variable ").append(key);
        String androidHome = System.getenv(key);
        if (androidHome == null) {
            err.append(" was not set.");
            return null;
        }

        err.append(" = ").append(androidHome);
        File androidHomeDir = new File(androidHome);
        if (!androidHomeDir.exists()) {
            err.append("\nHowever, that directory does not seem to exist.");
            return null;
        }

        if (!androidHomeDir.isDirectory()) {
            err.append("\nHowever, that path does not seem to be a directory.");
            return null;
        }

        return androidHomeDir;
    }

    private File                         homeDir;
    private int                          sdkRelease;
    private boolean                      valid;
    private Map<String, AndroidPlatform> platforms;

    public LocalAndroidPlatforms(File dir) throws IOException {
        this.homeDir = dir;
        this.valid = false;

        this.platforms = new HashMap<String, AndroidPlatform>();
        if (!this.homeDir.exists()) {
            LOG.warning("Directory does not exist: " + this.homeDir);
            return;
        }

        loadToolsDir();
        loadPlatformsDir();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LocalAndroidPlatforms [homeDir=");
        builder.append(homeDir);
        builder.append(", sdkRelease=");
        builder.append(sdkRelease);
        builder.append(", platforms=[");

        boolean delim = false;
        for (AndroidPlatform platform : platforms.values()) {
            if (delim) {
                builder.append(",");
            }
            builder.append(platform.getId());
            delim = true;
        }

        builder.append("]]");
        return builder.toString();
    }

    public void addPlatform(AndroidPlatform platform) {
        if (platform == null) {
            return;
        }
        this.platforms.put(platform.getId(), platform);
    }

    public boolean valid() {
        return valid;
    }

    public boolean exists() {
        return homeDir.exists();
    }

    public AndroidPlatform getPlatform(String id) throws AndroidPlatformNotFoundException {
        AndroidPlatform platform = platforms.get(id);
        if (platform == null) {
            throw new AndroidPlatformNotFoundException(id);
        }
        return platform;
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
        this.valid = true;
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

    public File getDir() {
        return this.homeDir;
    }

    public File getBin(String binname) throws FileNotFoundException {
        String paths[] = { "tools", "platform-tools" };

        String binosname = binname;
        if (SystemUtils.IS_OS_WINDOWS) {
            binosname += ".exe";
        }

        for (String searchPath : paths) {
            File bindir = new File(this.homeDir, searchPath);
            if (!bindir.exists()) {
                continue; // skip, dir does not exist.
            }
            File bin = new File(bindir, binosname);
            if (bin.exists() && bin.isFile() && bin.canExecute()) {
                return bin;
            }
        }
        throw new FileNotFoundException("android binary: " + binname);
    }

    public boolean hasApiLevel(String apilevel) {
        for (AndroidPlatform platform : platforms.values()) {
            if (StringUtils.equals(String.valueOf(platform.getApiLevel()), apilevel)) {
                return true;
            }
        }
        return false;
    }
}

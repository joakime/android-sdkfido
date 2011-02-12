package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.SystemUtils;

public final class Config {
    public static final String      HOME_DIR_NAME            = ".sdkfido";
    private static final String     KEY_ANDROID_SDK_DIR      = "android.sdk.dir";
    private static final String     KEY_PROJECTS_DIR         = "projects.dir";
    private static final String     KEY_WORK_DIR             = "work.dir";
    private static final Logger     LOG                      = Logger.getLogger(Config.class.getName());
    private PropertiesConfiguration config;
    private File                    configHome;

    public Config() {
        super();
        File configFile = new File(getConfigHome(), "sdkfido.properties");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                LOG.log(Level.FINE, "Unable to create empty configuration file: " + configFile, e);
            }
        }
        loadConfig(configFile);
    }

    public Config(File configFile) {
        super();
        loadConfig(configFile);
    }

    private boolean getBoolean(String key, boolean defValue) {
        return config.getBoolean(key, defValue);
    }

    public File getConfigHome() {
        if (configHome == null) {
            configHome = new File(SystemUtils.getUserHome(), HOME_DIR_NAME);
            if (!configHome.exists()) {
                configHome.mkdirs();
            }
        }
        return configHome;
    }

    private final void loadConfig(File configFile) {
        try {
            config = new PropertiesConfiguration(configFile);
        } catch (ConfigurationException e) {
            LOG.log(Level.WARNING, "Unable to load configuration: " + configFile, e);
        }
    }

    public void save() {
        try {
            config.save();
        } catch (ConfigurationException e) {
            LOG.log(Level.WARNING, "Unable to save configuration", e);
        }
    }

    private void setBoolean(String key, boolean value) {
        config.setProperty(key, Boolean.toString(value));
    }

    private void setDir(String key, File dir) {
        config.setProperty(key, dir.getAbsolutePath());
    }

    public void setPlatformsDir(File dir) {
        setDir(KEY_ANDROID_SDK_DIR, dir);
    }

    public void setWorkDir(File dir) {
        setDir(KEY_WORK_DIR, dir);
    }

    private File getDir(String key) {
        String dirname = config.getString(key);
        if (dirname == null) {
            return null;
        }
        return new File(dirname);
    }

    public void setProjectsDir(File dir) {
        setDir(KEY_PROJECTS_DIR, dir);
    }

    public File getProjectsDir() {
        File dir = getDir(KEY_PROJECTS_DIR);

        if (dir == null) {
            dir = new File(this.configHome, "projects");
            setProjectsDir(dir);
        }

        return dir;
    }

    public File getWorkDir() {
        File dir = getDir(KEY_WORK_DIR);

        if (dir == null) {
            dir = new File(getConfigHome(), "work");
            setWorkDir(dir);
        }

        return dir;
    }

    public File getPlatformsDir() throws IOException {
        File dir = getDir(KEY_ANDROID_SDK_DIR);

        if (dir == null) {
            dir = LocalAndroidPlatforms.findLocalJavaSdk();
            if (dir != null) {
                setPlatformsDir(dir);
            }
        }

        return dir;
    }
}

package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.SystemUtils;

public final class Config {
    private static final Logger     LOG = Logger.getLogger(Config.class.getName());
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

    public boolean getBoolean(String key, boolean defValue) {
        return config.getBoolean(key, defValue);
    }

    public File getConfigHome() {
        if (configHome == null) {
            configHome = new File(SystemUtils.getUserHome(), ".sdkfido");
            if (!configHome.exists()) {
                configHome.mkdirs();
            }
        }
        return configHome;
    }

    public File getFile(String key) {
        String value = config.getString(key);
        if (value == null) {
            return null;
        }
        return new File(value);
    }

    public File getFile(String key, File defaultFile) {
        String value = config.getString(key);
        if (value == null) {
            return defaultFile;
        }
        return new File(value);
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

    public void setBoolean(String key, boolean value) {
        config.setProperty(key, Boolean.toString(value));
        save();
    }
}

package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.SystemUtils;

public final class Config {
    private static final Logger    LOG = Logger.getLogger(Config.class.getName());
    private CompositeConfiguration config;
    private File                   configHome;

    public Config() {
        config = new CompositeConfiguration();
        File configFile = new File(getConfigHome(), "sdkfido.properties");
        loadConfig(configFile);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                LOG.log(Level.FINE, "Unable to create empty configuration file: " + configFile, e);
            }
        }
    }

    public Config(File configFile) {
        loadConfig(configFile);
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
            config.addConfiguration(new PropertiesConfiguration(configFile));
        } catch (ConfigurationException e) {
            LOG.log(Level.WARNING, "Unable to load configuration: " + configFile, e);
        }
    }
}

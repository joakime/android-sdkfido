package net.erdfelt.android.sdkfido.configer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

public class Configer {
    private static final Logger       LOG           = Logger.getLogger(Configer.class.getName());
    private BeanUtilsBean             bub;
    private Object                    obj;
    private Map<String, Configurable> configurables = new HashMap<String, Configurable>();
    // File to persist configuration to/from
    private File                      persistFile;
    // Resource Bundle for various descriptive definitions of the configurable fields
    private ResourceBundle            defsBundle;
    private Set<String>               scopes        = new TreeSet<String>();
    private Method                    rawArgAdder;

    public Configer(Object o) {
        this.obj = o;
        this.bub = BeanUtilsBean.getInstance();

        // Setup default persistent storage file
        File homeRcDir = new File(SystemUtils.getUserHome(), ".sdkfido");
        this.persistFile = new File(homeRcDir, "config.properties");

        // Load descriptive definitions
        try {
            defsBundle = ResourceBundle.getBundle(o.getClass().getName());
            Enumeration<String> keys = defsBundle.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                if (key.startsWith("scope.")) {
                    String scope = key.substring("scope.".length());
                    scopes.add(scope);
                }
            }
        } catch (MissingResourceException e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
        }

        // Identify all of the configurable fields
        walkFields(null, o.getClass(), null);

        // Identify the method to use for accepting raw arguments
        findRawArgsMethod(o.getClass());
    }

    public void addManualConfigurable(String option, String type, String description) {
        String key = option;
        if (key.startsWith("--")) {
            key = key.substring(2);
        }
        Configurable cfrb = new Configurable(key, type, description);
        this.configurables.put(key, cfrb);
    }

    public Configurable getConfigurable(String key) {
        return this.configurables.get(key);
    }

    public Set<Configurable> getConfigurables() {
        Set<Configurable> confs = new TreeSet<Configurable>(new ConfigurableComparator());
        confs.addAll(configurables.values());
        return confs;
    }

    public String getDefinition(String key) {
        try {
            return defsBundle.getString(key);
        } catch (MissingResourceException e) {
            LOG.warning("Unable to find description under ResourceBundle key: " + key);
            return "";
        }
    }

    public File getPersistFile() {
        return persistFile;
    }

    public String getScopeDescription(String scope) {
        return getDefinition("scope." + scope);
    }

    public List<String> getScopeIds() {
        List<String> scopelist = new ArrayList<String>(scopes);
        Collections.sort(scopelist);
        return scopelist;
    }

    public String getValue(String key) {
        Configurable cbl = configurables.get(key);
        if (cbl == null) {
            return null;
        }
        try {
            return this.bub.getProperty(this.obj, key);
        } catch (Throwable t) {
            LOG.log(Level.WARNING, "Unable to get value for key: " + key, t);
            return null;
        }
    }

    public boolean hasConfigurable(String key) {
        return configurables.containsKey(key);
    }

    private void findRawArgsMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            ConfigArguments arg = method.getAnnotation(ConfigArguments.class);
            if (arg == null) {
                continue; // skip, not tagged
            }

            int mods = method.getModifiers();
            if (!Modifier.isPublic(mods)) {
                continue; // skip, not public
            }

            if (Modifier.isStatic(mods)) {
                continue; // skip, dont support static
            }

            Class<?>[] params = method.getParameterTypes();
            if (params == null) {
                continue; // skip, needs params
            }

            if (params.length != 1) {
                continue; // skip, needs 1 param
            }

            if (!(params[0].equals(String.class))) {
                continue; // skip, param must be String
            }

            if (this.rawArgAdder != null) {
                StringBuilder err = new StringBuilder();
                err.append("Not allowed to have multiple @ConfigArguments defined: ");
                err.append("\n  Duplicate found at ").append(clazz.getName()).append("#").append(method.getName());
                err.append("\n  Original found at ").append(rawArgAdder.getDeclaringClass().getName()).append("#")
                        .append(rawArgAdder.getName());
                throw new IllegalStateException(err.toString());
            }

            this.rawArgAdder = method;
        }
    }

    private final void walkFields(String prefix, Class<?> clazz, String scope) {
        for (Field field : clazz.getDeclaredFields()) {
            ConfigOption opt = field.getAnnotation(ConfigOption.class);
            if (opt == null) {
                continue; // skip
            }

            String key = StringUtils.defaultString(prefix) + field.getName();
            if (opt.suboption()) {
                walkFields(key + ".", field.getType(), opt.scope());
            } else {
                Configurable cfgrbl = new Configurable(field, opt, key, scope);
                configurables.put(key, cfgrbl);

                scopes.add(cfgrbl.getScope());

                if (field.getType().isEnum()) {
                    // register converter for this field type.
                    Class<?> enumclass = field.getType();
                    this.bub.getConvertUtils().register(new EnumConverter(enumclass), enumclass);
                }
            }
        }
    }

    public void persist() throws IOException {
        Properties props = new Properties();

        String key, value;
        for (Configurable conf : configurables.values()) {
            if (conf.isInternal()) {
                continue; // skip Configuration internal configurables
            }
            key = conf.getKey();
            value = getValue(key);
            props.put(key, StringUtils.defaultString(value));
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(this.persistFile);
            props.store(writer, "Written by " + this.getClass().getName());
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * Restore from persistent storage.
     * 
     * @throws IOException
     *             if unable to read persisted storage file.
     * @see #setPersistFile(File)
     * @see #persist()
     */
    public void restore() throws IOException {
        if (!this.persistFile.exists()) {
            return; // nothing to load. skip it.
        }
        FileReader reader = null;
        try {
            reader = new FileReader(this.persistFile);
            Properties props = new Properties();
            props.load(reader);

            @SuppressWarnings("unchecked")
            Enumeration<String> keys = (Enumeration<String>) props.propertyNames();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                setValue(key, props.getProperty(key));
            }
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public void setPersistFile(File persistFile) {
        if (persistFile == null) {
            throw new NullPointerException("persistFile cannot be null");
        }
        this.persistFile = persistFile;
    }

    public void setValue(String key, String value) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key is unset or blank");
        }
        Configurable cbl = configurables.get(key);
        if (cbl == null) {
            throw new IllegalArgumentException("Not a valid configurable key [" + key + "]");
        }

        try {
            this.bub.setProperty(this.obj, key, value);
        } catch (Throwable t) {
            LOG.log(Level.WARNING, t.getMessage(), t);
        }
    }

    public boolean hasRawArgsDefined() {
        return (rawArgAdder != null);
    }

    public void addRawArg(String arg) {
        if (rawArgAdder == null) {
            return;
        }

        Object params[] = { arg };

        try {
            rawArgAdder.invoke(this.obj, params);
        } catch (Throwable t) {
            LOG.log(Level.WARNING,
                    "Unable to add raw arg: " + this.obj.getClass().getName() + "#" + rawArgAdder.getName(), t);
        }
    }

    public String getRawArgsName() {
        if (rawArgAdder != null) {
            ConfigArguments ca = rawArgAdder.getAnnotation(ConfigArguments.class);
            if ((ca != null) && (StringUtils.isNotBlank(ca.name()))) {
                return ca.name();
            }
        }
        return "arguments";
    }
}

package net.erdfelt.android.sdkfido.configer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;

public class ConfigCmdLineParser {
    public static final String OPT_HELP   = "help";
    public static final String OPT_SAVE   = "save-config";
    public static final String OPT_CONFIG = "config";

    private Configer           configer;
    private Class<?>           mainclass;
    private PrintWriter        out;

    public ConfigCmdLineParser(Object mainclassobj, Object obj) {
        this(mainclassobj.getClass(), obj);
    }

    public ConfigCmdLineParser(Class<?> mainclass, Object obj) {
        this.mainclass = mainclass;
        this.configer = new Configer(obj);

        this.configer.addManualConfigurable(OPT_HELP, null, "show this help screen");
        this.configer.addManualConfigurable(OPT_SAVE, null, "save configuration options to disk");
        this.configer.addManualConfigurable(OPT_CONFIG, "File", "the filename to persist configuration options in");
        this.out = new PrintWriter(System.out);
    }

    public void parse(String[] args) throws CmdLineParseException {
        LinkedList<String> arglist = new LinkedList<String>();
        arglist.addAll(Arrays.asList(args));

        // Quick Help
        if (arglist.contains("--" + OPT_HELP)) {
            usage();
            return;
        }

        // Configuration File Option
        int idx = arglist.indexOf("--" + OPT_CONFIG);
        if (idx >= 0) {
            if (idx + 1 > arglist.size()) {
                throw new CmdLineParseException("Expected <File> parameter for option: --" + OPT_CONFIG);
            }
            String value = arglist.get(idx + 1);
            File file = (File) ConvertUtils.convert(value, File.class);

            this.configer.setPersistFile(file);

            arglist.remove(idx + 1);
            arglist.remove(idx);
        }

        // Save Options Option
        boolean saveOptions = false;

        idx = arglist.indexOf("--" + OPT_SAVE);
        if (idx >= 0) {
            saveOptions = true;
            arglist.remove(idx);
        }

        // Restore from persist first.
        try {
            configer.restore();
        } catch (IOException e) {
            throw new CmdLineParseException("Unable to load configuration: " + e.getMessage(), e);
        }

        // Set values from command line now.
        String value;
        ListIterator<String> iter = arglist.listIterator();
        while (iter.hasNext()) {
            String arg = iter.next();
            if (arg.startsWith("--")) {
                // Its an option.
                String optname = arg.substring(2);

                Configurable cfgrbl = configer.getConfigurable(optname);

                if (cfgrbl == null) {
                    throw new CmdLineParseException("Invalid Option: " + arg);
                }

                if (!iter.hasNext()) {
                    throw new CmdLineParseException("Expected <" + cfgrbl.getType() + "> parameter for option: " + arg);
                }
                value = iter.next();
                configer.setValue(optname, value);
                continue; // process next arg
            }

            // All others are considered args.
            addToRawArgs(arg);
        }

        // Save options (if specified)
        if (saveOptions) {
            try {
                configer.persist();
            } catch (IOException e) {
                throw new CmdLineParseException("Unable to save configuration: " + e.getMessage(), e);
            }
        }
    }

    private void addToRawArgs(String arg) throws CmdLineParseException {
        if (!configer.hasRawArgsDefined()) {
            throw new CmdLineParseException("No @ConfigArguments annotation setup for raw arguments.");
        }
        configer.addRawArg(arg);
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public void setOut(Writer writer) {
        if (writer instanceof PrintWriter) {
            this.out = (PrintWriter) writer;
        } else {
            this.out = new PrintWriter(writer);
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    private String toOptionExample(Configurable cfgr) {
        StringBuilder opt = new StringBuilder();
        opt.append("--").append(cfgr.getKey());

        if (cfgr.hasType()) {
            opt.append(" <").append(cfgr.getType()).append(">");
        }

        return opt.toString();
    }

    private String toSectionHeader(String message) {
        return StringUtils.rightPad(String.format(".\\ %s \\.", message), 78, '_');
    }

    public void usage() {
        int maxOptSize = 0;
        for (Configurable cfgr : configer.getConfigurables()) {
            maxOptSize = Math.max(maxOptSize, toOptionExample(cfgr).length());
        }

        String argname = configer.getRawArgsName();
        out.printf("Usage: java -jar " + mainclass.getName() + " [options...] [%s...]%n", argname);

        List<String> scopes = configer.getScopeIds();
        for (String scope : scopes) {
            out.println();
            out.println(toSectionHeader(configer.getScopeDescription(scope)));
            for (Configurable cfgr : configer.getConfigurables()) {
                if (StringUtils.equals(cfgr.getScope(), scope)) {
                    usage(cfgr, maxOptSize);
                }
            }
        }

        out.flush();
    }

    private void usage(Configurable cfgr, int maxOptSize) {
        out.print(StringUtils.rightPad(toOptionExample(cfgr), maxOptSize, ' '));

        out.printf(" : %s%n", cfgr.getDescription());
        String indent = StringUtils.rightPad("", maxOptSize + 3);

        if (cfgr.getField() == null) {
            // Manually Added Configurable.
            if (OPT_CONFIG.equals(cfgr.getKey())) {
                out.printf("%s(default value: %s)%n", indent, configer.getPersistFile().getAbsolutePath());
            }
            out.flush();
            return;
        }

        if (cfgr.getField().getType().isEnum()) {
            for (Field f : cfgr.getField().getType().getDeclaredFields()) {
                if (f.isEnumConstant()) {
                    out.printf("%s\"%s\"", indent, f.getName());
                    ConfigOption copt = f.getAnnotation(ConfigOption.class);
                    if (copt != null) {
                        out.printf(" - %s", copt.description());
                    }
                    out.println();
                }
            }
        }

        Object obj = configer.getValue(cfgr.getName());
        if (obj != null) {
            out.printf("%s(default value: %s)%n", indent, obj);
        }
        out.flush();
    }

    /**
     * Variant of {@link #usage()} but prints the failure as the error message, then the usage, then the stacktrace
     * information.
     * 
     * @param e
     */
    public void usage(CmdLineParseException e) {
        out.println("ERROR: " + e.getMessage());
        usage();
        e.printStackTrace(out);
        out.flush();
    }
}

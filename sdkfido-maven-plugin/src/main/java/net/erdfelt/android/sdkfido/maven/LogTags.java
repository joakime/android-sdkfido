package net.erdfelt.android.sdkfido.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.IOUtil;

public class LogTags {
    private static final String  COMMENT_PATTERN = "^\\s*(#.*)?$";
    private static final String  OPTION_PATTERN  = "^\\s*option\\s+(\\w+)\\s*(.*)$";
    private static final String  TAG_PATTERN     = "^\\s*(\\d+)\\s+(\\w+)\\s*(\\(.*\\))?\\s*$";

    private Map<Integer, String> codes           = new TreeMap<Integer, String>();
    private String               packageName;
    private String               className;

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public LogTags(File file) throws IOException {
        Pattern comment = Pattern.compile(COMMENT_PATTERN);
        Pattern option = Pattern.compile(OPTION_PATTERN);
        Pattern tag = Pattern.compile(TAG_PATTERN);

        this.className = file.getName().replaceFirst("\\.logtags$", "");

        FileReader reader = null;
        BufferedReader buf = null;

        try {
            reader = new FileReader(file);
            buf = new BufferedReader(reader);

            String line = null;
            Matcher m;
            while ((line = buf.readLine()) != null) {
                m = comment.matcher(line);
                if (m.matches()) {
                    continue;
                }

                m = tag.matcher(line);
                if (m.matches()) {
                    try {
                        int num = Integer.parseInt(m.group(1));
                        String name = m.group(2);
                        codes.put(num, name);
                    } catch (NumberFormatException e) {
                        throw new IOException("Unable to parse num: " + line, e);
                    }
                    continue;
                }

                m = option.matcher(line);
                if (m.matches()) {
                    String key = m.group(1);
                    String val = m.group(2);
                    if ("java_package".equals(key)) {
                        String name = val.trim();
                        name = name.replaceFirst("\\;$", "");
                        this.packageName = name;
                    }
                    continue;
                }

                System.out.printf("Unexpected entry in file: %s : %s", file, line);
                continue;

            }
        } finally {
            IOUtil.close(buf);
            IOUtil.close(reader);
        }
    }

    public void writeJavaSource(File javaSource) throws IOException {
        FileWriter writer = null;
        PrintWriter out = null;
        try {
            writer = new FileWriter(javaSource);
            out = new PrintWriter(writer);

            out.printf("package %s;%n", this.packageName);
            out.println();
            out.printf("public final class %s {%n", this.className);
            for (Map.Entry<Integer, String> entry : codes.entrySet()) {
                out.printf("    public static final int %s = %d;%n", entry.getValue().toUpperCase(), entry.getKey());
            }
            out.println("}");
        } finally {
            IOUtil.close(out);
            IOUtil.close(writer);
        }
    }
}

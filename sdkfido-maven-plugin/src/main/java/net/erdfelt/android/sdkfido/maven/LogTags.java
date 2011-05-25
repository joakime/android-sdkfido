package net.erdfelt.android.sdkfido.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.IOUtil;

public class LogTags {
    private static final String  COMMENT_PATTERN = "^\\s*(#.*)?$";
    private static final String  TAG_PATTERN     = "^\\s*(\\d+)\\s+(\\w+)\\s*(\\(.*\\))?\\s*$";

    private Map<Integer, String> codes           = new HashMap<Integer, String>();

    public LogTags(Log log, File file) throws IOException {
        Pattern comment = Pattern.compile(COMMENT_PATTERN);
        Pattern tag = Pattern.compile(TAG_PATTERN);

        FileReader reader = null;
        BufferedReader buf = null;

        try {
            reader = new FileReader(file);
            buf = new BufferedReader(reader);

            String line = null;
            while ((line = buf.readLine()) != null) {
                if (comment.matcher(line).matches())
                    continue;

                Matcher m = tag.matcher(line);
                if (!m.matches()) {
                    log.warn("Unexpected entry in file: " + file + " : " + line);
                }

                try {
                    int num = Integer.parseInt(m.group(1));
                    String name = m.group(2);
                    codes.put(num, name);
                } catch (NumberFormatException e) {
                    throw new IOException("Unable to parse num: " + line, e);
                }
            }
        } finally {
            IOUtil.close(buf);
            IOUtil.close(reader);
        }
    }

    public void writeJavaSource(File javaSource, String packageName, String className) throws IOException {
        // TODO: write simple java source file.
    }
}

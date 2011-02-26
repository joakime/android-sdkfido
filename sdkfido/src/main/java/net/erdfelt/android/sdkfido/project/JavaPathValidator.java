package net.erdfelt.android.sdkfido.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Simple validation of the java files found in a provided source directory to ensure that their relative paths (to the
 * source directory) and the java <code>package</code> declarations make sense.
 */
public class JavaPathValidator {
    private static final Logger LOG = Logger.getLogger(JavaPathValidator.class.getName());
    private Pattern             packagePat;

    public JavaPathValidator() {
        this.packagePat = Pattern.compile("^package *\\([a-zA-Z0-9._]*\\).*$");
    }

    public int validateSourceTree(Dir sourceDir) throws IOException {
        List<String> javapaths = sourceDir.findFilePaths("^.*\\.java$");
        int count = 0; // Yeah, I know I can use javapaths.size(), but I wanted it to be sane here.
        for (String javapath : javapaths) {
            try {
                validateJavaPackage(sourceDir, javapath);
            } catch (IOException e) {
                throw new IOException("Failed to validate java path (#" + count + " of " + javapaths.size() + "): "
                        + javapath, e);
            }
            count++;
        }
        return count;
    }

    private void validateJavaPackage(Dir sourceDir, String javapath) throws IOException {
        File javafile = sourceDir.getFile(javapath);
        FileReader reader = null;
        BufferedReader buf = null;
        try {
            reader = new FileReader(javafile);
            buf = new BufferedReader(reader);
            String line;
            String actualPackage;
            String expectedPackage = javapath.substring(0, javapath.length() - javafile.getName().length());
            expectedPackage = expectedPackage.replace(File.separatorChar, '.');
            Matcher mat;

            while ((line = buf.readLine()) != null) {
                mat = packagePat.matcher(line);
                if (mat.matches()) {
                    actualPackage = mat.group(1);
                    if (!StringUtils.equals(actualPackage, expectedPackage)) {
                        LOG.warning("" + javapath + " - NO MATCH TO " + expectedPackage);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(buf);
            IOUtils.closeQuietly(reader);
        }
    }
}

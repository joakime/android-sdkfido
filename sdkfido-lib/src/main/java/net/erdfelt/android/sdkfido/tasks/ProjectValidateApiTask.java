package net.erdfelt.android.sdkfido.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.project.OutputProject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class ProjectValidateApiTask implements Task {
    private static final Logger LOG = Logger.getLogger(ProjectValidateApiTask.class.getName());
    private OutputProject       project;
    private String              apilevel;
    private Pattern             packagePat;

    public ProjectValidateApiTask(OutputProject project, String apilevel) {
        this.project = project;
        this.apilevel = apilevel;
        this.packagePat = Pattern.compile("^package *\\([a-zA-Z0-9._]*\\).*$");
    }

    @Override
    public String getName() {
        return "Validate Java 'package' declarations";
    }

    @Override
    public void run(TaskQueue tasks) throws Throwable {
        List<String> javapaths = project.getSourceDir().findFilePaths("^.*\\.java$");
        for (String javapath : javapaths) {
            validateJavaPackage(javapath);
        }
    }

    private void validateJavaPackage(String javapath) {
        File javafile = project.getSourceDir().getFile(javapath);
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
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Unable to read java file: " + javapath, e);
        } finally {
            IOUtils.closeQuietly(buf);
            IOUtils.closeQuietly(reader);
        }
    }
}

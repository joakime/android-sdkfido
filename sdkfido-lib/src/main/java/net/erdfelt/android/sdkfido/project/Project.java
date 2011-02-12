package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.sdks.Sdk;
import net.erdfelt.android.sdkfido.util.PathUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Project {
    public static final String  COPIED_SOURCE_LOG = "copied-source.log";
    private static final Logger LOG               = Logger.getLogger(Project.class.getName());
    private File                baseDir;
    private File                srcJava;
    private File                srcResources;
    private String              id;

    public Project(File projectsDir, Sdk sdk) {
        this.id = sdk.getVersion();
        this.baseDir = new File(projectsDir, "android-" + sdk.getVersion());
        this.srcJava = new File(baseDir, FilenameUtils.separatorsToSystem("src/main/java"));
        this.srcResources = new File(baseDir, FilenameUtils.separatorsToSystem("src/main/resources"));
    }

    public File getSrcJava() {
        return srcJava;
    }

    public File getSrcJava(String path) {
        return new File(srcJava, FilenameUtils.separatorsToSystem(path));
    }

    public File getSrcResources() {
        return srcResources;
    }

    public File getSrcResource(String path) {
        return new File(srcResources, FilenameUtils.separatorsToSystem(path));
    }

    public void create() {
        mkdirs(baseDir);
        mkdirs(srcJava);
        mkdirs(srcResources);
    }

    private void mkdirs(File dir) {
        if (dir.exists()) {
            return;
        }

        dir.mkdirs();
    }

    public File getBaseDir() {
        return baseDir;
    }

    public String getId() {
        return id;
    }
    
    public File getSubPath(String path) {
        return new File(baseDir, FilenameUtils.separatorsToSystem(path));
    }

    public void delete(String path) {
        File file = getSubPath(path);
        if (file.exists()) {
            file.delete();
        }
    }

    private void cleanTree(File dir) {
        if (dir.exists()) {
            try {
                FileUtils.cleanDirectory(dir);
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Unable to clean directory: " + dir, e);
            }
        } else {
            dir.mkdirs();
        }
    }

    public void cleanSourceTree() {
        cleanTree(getSrcJava());
        cleanTree(getSrcResources());
        cleanTree(getSubPath("target/classes"));
    }

    public void copyStub(File androidJarFile) throws IOException {
        File destfile = getSubPath("lib/android-stub.jar");
        FileUtils.forceMkdir(destfile.getParentFile());
        FileUtils.copyFile(androidJarFile, destfile);
    }

    public List<String> getResourcesPathsOfType(String ext) {
        List<String> paths = new ArrayList<String>();
        recursePathTypes(paths, getSrcResources(), getSrcResources(), ext);
        return paths;
    }

    private void recursePathTypes(List<String> paths, File basedir, File dir, String ext) {
        for (File path : dir.listFiles()) {
            if (path.isDirectory()) {
                recursePathTypes(paths, basedir, path, ext);
            } else if (path.isFile()) {
                if (path.getName().endsWith(ext)) {
                    paths.add(PathUtil.toRelativePath(basedir, path));
                }
            }
        }
    }
}

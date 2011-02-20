package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import net.erdfelt.android.sdkfido.util.PathUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Dir {
    private File basedir;

    public Dir(File dir) {
        this.basedir = dir;
    }

    public Dir(File dir, CharSequence name) {
        this(new File(dir, name.toString()));
    }

    public File getPath() {
        return this.basedir;
    }

    public File getFile(String path) {
        return new File(this.basedir, FilenameUtils.separatorsToSystem(path));
    }

    public void delete(String path) throws IOException {
        File file = getFile(path);
        if (!file.exists()) {
            return; // Nothing to delete.
        }
        FileUtils.forceDelete(file);
    }

    public Dir getSubDir(String path) {
        return new Dir(new File(this.basedir, FilenameUtils.separatorsToSystem(path)));
    }

    public void ensureEmpty() throws IOException {
        if (basedir.exists()) {
            FileUtils.cleanDirectory(basedir);
        } else {
            FileUtils.forceMkdir(basedir);
        }
    }

    public void ensureExists() throws IOException {
        if (basedir.exists()) {
            return;
        }

        FileUtils.forceMkdir(basedir);
    }

    public List<String> findFilePaths(String filenameRegex) {
        List<String> paths = new ArrayList<String>();
        Pattern pat = Pattern.compile(filenameRegex);
        recurseFilePaths(paths, this.basedir, pat);
        Collections.sort(paths);
        return paths;
    }

    private void recurseFilePaths(List<String> paths, File dir, Pattern filenamePattern) {
        for (File path : dir.listFiles()) {
            if (path.isDirectory()) {
                recurseFilePaths(paths, path, filenamePattern);
            } else if (path.isFile()) {
                if (filenamePattern.matcher(path.getName()).matches()) {
                    paths.add(PathUtil.toRelativePath(basedir, path));
                }
            }
        }
    }

    public String getRelativePath(Dir otherDir) {
        return getRelativePath(otherDir.getPath());
    }

    public String getRelativePath(File otherPath) {
        return PathUtil.toRelativePath(basedir, otherPath);
    }
}

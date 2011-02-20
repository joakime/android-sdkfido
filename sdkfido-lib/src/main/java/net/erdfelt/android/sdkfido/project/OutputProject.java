package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.util.List;

public abstract class OutputProject {

    public void start() {
        // TODO Auto-generated method stub

    }

    protected void create() {
        // TODO Auto-generated method stub

    }

    protected void cleanSourceTree() {
        // TODO Auto-generated method stub

    }

    public void copySource(File sourceDir, String basedirProjectId) {
        // TODO Auto-generated method stub

    }

    public void init() {
        create();
        cleanSourceTree();
    }

    public void close() {
        // TODO Auto-generated method stub

    }

    public List<String> getPathsOfType(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    public File getSrcJava(String javapath) {
        // TODO Auto-generated method stub
        return null;
    }
}

package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.FetchTarget;

public class AntOutputProject implements OutputProject {
    private Dir baseDir;
    private Dir srcDir;

    public AntOutputProject(File projectsDir, FetchTarget target) {
        StringBuilder filename = new StringBuilder();
        filename.append("android-");
        switch (target.getType()) {
            case APILEVEL:
                filename.append("api-");
                break;
            case BRANCH:
            case TAG:
            case VERSION:
                filename.append(target.getType().name().toLowerCase()).append("-");
                break;
        }
        filename.append(target.getId());
        this.baseDir = new Dir(projectsDir, filename);
        this.srcDir = this.baseDir.getSubDir("src");
    }

    @Override
    public String toString() {
        return String.format("Ant: %s", baseDir.getPath().getName());
    }

    @Override
    public void init() throws IOException {
        this.baseDir.ensureExists();
        this.srcDir.ensureExists();
    }

    @Override
    public void close() throws IOException {
        // TODO: compile aidl?
        // TODO: copy & filter ant build.xml
    }

    @Override
    public void startSubProject(String projectId) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void copySource(File gitIncludeDir) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public Dir getBaseDir() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dir getSourceDir() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dir getResourceDir() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dir getOutputDir() {
        // TODO Auto-generated method stub
        return null;
    }
}
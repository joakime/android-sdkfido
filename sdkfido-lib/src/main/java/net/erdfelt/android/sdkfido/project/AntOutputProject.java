package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.FetchTarget;

public class AntOutputProject extends AbstractOutputProject implements OutputProject {

    public AntOutputProject(File projectsDir, FetchTarget target) {
        baseDir = new Dir(projectsDir, toBaseDirName(target));
        sourceDir = this.baseDir.getSubDir("src");
    }

    @Override
    public String toString() {
        return String.format("Ant: %s", baseDir.getPath().getName());
    }

    @Override
    public void init() throws IOException {
        baseDir.ensureExists();
        sourceDir.ensureExists();
    }

    @Override
    public void close() throws IOException {
        // TODO: compile aidl?
        // TODO: copy & filter ant build.xml
    }

    @Override
    public void copySource(File gitIncludeDir) throws IOException {
        // TODO Auto-generated method stub

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
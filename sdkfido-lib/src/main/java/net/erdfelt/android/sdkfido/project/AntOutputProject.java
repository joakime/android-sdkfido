package net.erdfelt.android.sdkfido.project;

import java.io.File;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;

public class AntOutputProject extends AbstractOutputProject implements OutputProject {
    private Dir sourceDir;

    public AntOutputProject(File projectsDir, FetchTarget target) {
        baseDir = new Dir(projectsDir, toBaseDirName(target));
        sourceDir = this.baseDir.getSubDir("src");
    }

    @Override
    public String toString() {
        return String.format("Ant: %s", baseDir.getPath().getAbsolutePath());
    }

    @Override
    public void init() throws FetchException {
        baseDir.ensureExists();
        sourceDir.ensureExists();
    }

    @Override
    public void close() throws FetchException {
        // TODO: compile aidl?
        // TODO: copy & filter ant build.xml
    }

    @Override
    public void copySource(File gitIncludeDir) throws FetchException {
        // TODO Auto-generated method stub

    }
}
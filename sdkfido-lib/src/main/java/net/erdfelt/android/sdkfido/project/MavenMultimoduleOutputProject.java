package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;

public class MavenMultimoduleOutputProject extends AbstractOutputProject implements OutputProject {
    private Dir sourceDir;
    private Dir resourceDir;
    private Dir outputDir;

    public MavenMultimoduleOutputProject(File projectDir, FetchTarget target) {
        baseDir = new Dir(projectDir, toBaseDirName(target));
        sourceDir = baseDir.getSubDir("src/main/java");
        resourceDir = baseDir.getSubDir("src/main/resources");
        outputDir = baseDir.getSubDir("target/classes");
    }

    @Override
    public void startSubProject(String projectId) throws FetchException {
        // TODO Auto-generated method stub
    }

    @Override
    public void copySource(File gitIncludeDir) throws FetchException {
        // TODO Auto-generated method stub
    }

    @Override
    public void init() throws FetchException {
        // TODO Auto-generated method stub
    }

    @Override
    public void close() throws FetchException {
        // TODO Auto-generated method stub
    }
}

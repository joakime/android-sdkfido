package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.FetchTarget;

public class MavenMultimoduleOutputProject implements OutputProject {

    public MavenMultimoduleOutputProject(File outputDir, FetchTarget target) {
        // TODO Auto-generated constructor stub
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

    @Override
    public void init() throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

}

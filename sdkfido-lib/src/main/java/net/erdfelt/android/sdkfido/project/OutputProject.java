package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;

public interface OutputProject {
    public void startSubProject(String projectId) throws IOException;

    public void copySource(File gitIncludeDir) throws IOException;

    public Dir getBaseDir();

    public Dir getSourceDir();

    public Dir getResourceDir();

    public Dir getOutputDir();

    public void init() throws IOException;

    public void close() throws IOException;
}

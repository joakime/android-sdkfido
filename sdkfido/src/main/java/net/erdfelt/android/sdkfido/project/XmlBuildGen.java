package net.erdfelt.android.sdkfido.project;

import java.io.File;

public interface XmlBuildGen {
    public void generate(File outputFile) throws OutputProjectException;
}

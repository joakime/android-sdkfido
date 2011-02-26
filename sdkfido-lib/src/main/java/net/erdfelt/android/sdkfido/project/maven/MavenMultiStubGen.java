package net.erdfelt.android.sdkfido.project.maven;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.project.FilteredFileUtil;
import net.erdfelt.android.sdkfido.project.OutputProjectException;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;

/**
 * Creates a maven <code>pom.xml</code> for the specialized 'android-stub' sub-module in a multi-module maven build.
 */
public class MavenMultiStubGen implements XmlBuildGen {
    private static final Logger LOG   = Logger.getLogger(MavenMultiStubGen.class.getName());
    private Map<String, String> props = new HashMap<String, String>();

    public MavenMultiStubGen(String parentArtifact, String subArtifact, String version, String apilevel) {
        props.put("GROUPID", MavenConstants.DEFAULT_GROUPID);
        props.put("PARENT_ARTIFACTID", parentArtifact);
        props.put("ARTIFACTID", subArtifact);
        props.put("VERSION", version);
        props.put("APILEVEL", apilevel);
    }

    @Override
    public void generate(File outputFile) throws OutputProjectException {
        LOG.info("Generating: " + outputFile);
        FilteredFileUtil.copyWithExpansion("maven-multi-stub.xml", outputFile, props);
    }
}

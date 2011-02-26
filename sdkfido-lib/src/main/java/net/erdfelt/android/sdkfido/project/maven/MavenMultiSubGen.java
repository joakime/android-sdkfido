package net.erdfelt.android.sdkfido.project.maven;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.project.FilteredFileUtil;
import net.erdfelt.android.sdkfido.project.OutputProjectException;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;

/**
 * Creates a maven <code>pom.xml</code> for a sub-module in a multi-module maven build.
 * <p>
 * This generated pom.xml performs the following:
 * <ul>
 * <li>generate-sources
 * <ul>
 * <li>exec:exec [aapt] (optional if ${project.basedir}/src/main/resources/AndroidManifest.xml exists)</li>
 * </ul>
 * </li>
 * <li>generate-resources
 * <ul>
 * <li>exec:exec [aidl-pre] (optional if *.aidl files exist)</li>
 * <li>exec:exec [aidl] (optional if *.aidl files exist)</li>
 * </ul>
 * </li>
 * <li>compile</li>
 * <li>source:jar</li>
 * <li>javadoc:jar</li>
 * <li>package</li> </li>
 */
public class MavenMultiSubGen implements XmlBuildGen {
    private static final Logger LOG   = Logger.getLogger(MavenMultiSubGen.class.getName());
    private Map<String, String> props = new HashMap<String, String>();

    public MavenMultiSubGen(String parentArtifact, String subArtifact, String version) {
        props.put("GROUPID", MavenConstants.DEFAULT_GROUPID);
        props.put("PARENT_ARTIFACTID", parentArtifact);
        props.put("ARTIFACTID", subArtifact);
        props.put("VERSION", version);
    }

    @Override
    public void generate(File outputFile) throws OutputProjectException {
        LOG.info("Generating: " + outputFile);
        FilteredFileUtil.copyWithExpansion("maven-multi-sub.xml", outputFile, props);
    }
}

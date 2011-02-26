package net.erdfelt.android.sdkfido.project.maven;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.FetchTarget;
import net.erdfelt.android.sdkfido.project.FilteredFileUtil;
import net.erdfelt.android.sdkfido.project.OutputProjectException;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;

/**
 * Creates a maven <code>pom.xml</code> for a single maven build.
 * <p>
 * This generated pom.xml performs no compile, simply packages up the existing sources and attaches them to the active
 * project to allow them to be deployed and/or released.
 */
public class MavenPackageGen implements XmlBuildGen {
    private static final Logger LOG   = Logger.getLogger(MavenPackageGen.class.getName());
    private Map<String, String> props = new HashMap<String, String>();

    public MavenPackageGen(FetchTarget target) {
        props.put("GROUPID", MavenConstants.DEFAULT_GROUPID);
        props.put("ARTIFACTID", MavenConstants.DEFAULT_ARTIFACTID);
        props.put("VERSION", target.getVersion().toString());
    }

    @Override
    public void generate(File outputFile) throws OutputProjectException {
        LOG.info("Generating: " + outputFile);
        FilteredFileUtil.copyWithExpansion("maven-pom.xml", outputFile, props);
    }
}

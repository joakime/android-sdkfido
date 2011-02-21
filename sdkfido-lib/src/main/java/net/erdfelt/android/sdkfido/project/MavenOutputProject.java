package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;
import net.erdfelt.android.sdkfido.local.JarListing;

import org.apache.commons.io.FileUtils;

public class MavenOutputProject extends AbstractOutputProject implements OutputProject {
    private static final Logger LOG = Logger.getLogger(MavenOutputProject.class.getName());
    private Dir                 sourceDir;
    private Dir                 resourceDir;
    private Dir                 outputDir;
    private SourceCopier        copier;
    private FetchTarget         target;
    private String              groupId;
    private String              artifactId;

    public MavenOutputProject(File projectDir, FetchTarget target) {
        baseDir = new Dir(projectDir, toBaseDirName(target));
        sourceDir = baseDir.getSubDir("src/main/java");
        resourceDir = baseDir.getSubDir("src/main/resources");
        outputDir = baseDir.getSubDir("target/classes");
        this.target = target;
        this.groupId = "com.android.sdk";
        this.artifactId = "android";
    }

    @Override
    public void close() throws FetchException {
        copier.close();

        try {
            JavaPathValidator validator = new JavaPathValidator();
            int count = validator.validateSourceTree(sourceDir);
            LOG.info(String.format("Validation Success! (output source tree structure of %,d java files)%n", count));
        } catch (IOException e) {
            throw new FetchException(
                    "Java Source Validation Failed!  You might not have a valid set of source! (see console for details on why)",
                    e);
        }

        // TODO: AIDL Compile?

        // Create build.xml
        Map<String, String> props = new HashMap<String, String>();
        props.put("GROUPID", this.groupId);
        props.put("ARTIFACTID", this.artifactId);
        props.put("VERSION", target.getVersion().toString());

        File pomFile = this.baseDir.getFile("pom.xml");
        FilteredFileUtil.copyWithExpansion("maven-pom.xml", pomFile, props);
    }

    @Override
    public void copySource(File gitIncludeDir) throws FetchException {
        try {
            copier.copyTree(gitIncludeDir, sourceDir, resourceDir);
        } catch (IOException e) {
            throw new FetchException("Unable to copy source tree: " + gitIncludeDir, e);
        }
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    @Override
    public void init() throws FetchException {
        baseDir.ensureExists();
        sourceDir.ensureExists();
        resourceDir.ensureExists();

        try {
            this.copier = new SourceCopier(baseDir);
        } catch (IOException e) {
            throw new FetchException(e.getMessage(), e);
        }
        if (androidStub != null) {
            try {
                Dir libDir = baseDir.getSubDir("lib");
                libDir.ensureExists();
                FileUtils.copyFile(androidStub, libDir.getFile("android-stub.jar"));
                copier.setNarrowSearchTo(new JarListing(androidStub));
            } catch (IOException e) {
                throw new FetchException(
                        "Unable to narrow search tree by using listing of java files withou android stub jar file: "
                                + androidStub, e);
            }
        }
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return String.format("Maven Output: %s", baseDir.getPath().getAbsolutePath());
    }
}

package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;

public class MavenOutputProject extends AbstractOutputProject implements OutputProject {
    private Dir         sourceDir;
    private Dir         resourceDir;
    private Dir         outputDir;
    private FetchTarget target;
    private String      groupId;
    private String      artifactId;

    public MavenOutputProject(File projectDir, FetchTarget target) {
        baseDir = new Dir(projectDir, toBaseDirName(target));
        sourceDir = baseDir.getSubDir("src/main/java");
        resourceDir = baseDir.getSubDir("src/main/resources");
        outputDir = baseDir.getSubDir("target/classes");
        this.target = target;
        this.groupId = "com.android.sdk";
        this.artifactId = "android";
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
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

        Map<String, String> props = new HashMap<String, String>();
        props.put("GROUPID", "com.android.sdkfido");
        props.put("ARTIFACTID", "android");
        props.put("VERSION", target.getVersion().toString());

        File pomFile = this.baseDir.getFile("pom.xml");
        FilteredFileUtil.copyWithExpansion("maven-pom.xml", pomFile, props);
    }
}

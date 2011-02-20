package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.erdfelt.android.sdkfido.FetchTarget;

public class MavenOutputProject implements OutputProject {
    private Dir         baseDir;
    private FetchTarget target;
    private String      groupId;
    private String      artifactId;

    public MavenOutputProject(File outputDir, FetchTarget target) {
        this.baseDir = new Dir(outputDir);
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

        Map<String, String> props = new HashMap<String, String>();
        props.put("GROUPID", "com.android.sdkfido");
        props.put("ARTIFACTID", "android");
        props.put("VERSION", target.getVersion().toString());

        File pomFile = this.baseDir.getFile("pom.xml");
        FilteredFileUtil.copyWithExpansion("maven-pom.xml", pomFile, props);
    }
}

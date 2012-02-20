/*******************************************************************************
 *    Copyright 2012 - Joakim Erdfelt
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.configer.ConfigArguments;
import net.erdfelt.android.sdkfido.configer.ConfigOption;
import net.erdfelt.android.sdkfido.local.LocalAndroidPlatforms;
import net.erdfelt.android.sdkfido.project.OutputProjectType;

import org.apache.commons.lang.SystemUtils;

public class FetcherConfig {
    public static class MavenConfig {
        @ConfigOption(description = "Maven Group ID")
        private String  groupId        = "com.android.sdk";
        @ConfigOption(description = "Maven Artifact ID (used as prefix in MAVEN_MULTI ProjectType)")
        private String  artifactId     = "android";
        @ConfigOption(description = "Include SDK android.jar (stub) as attached artifact in build")
        private boolean includeStubJar = true;

        public String getArtifactId() {
            return artifactId;
        }

        public String getGroupId() {
            return groupId;
        }

        public boolean isIncludeStubJar() {
            return includeStubJar;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public void setIncludeStubJar(boolean includeStubJar) {
            this.includeStubJar = includeStubJar;
        }
    }

    private static final Logger LOG        = Logger.getLogger(FetcherConfig.class.getName());

    @ConfigOption(description = "Work Directory", type = "Dir")
    private File                workDir;

    @ConfigOption(description = "Location of the Android SDK (that has platforms dir)", type = "Dir")
    private File                platformsDir;

    @ConfigOption(description = "Output Project Type", type = "ProjectType")
    private OutputProjectType   outputType = OutputProjectType.SDK;

    @ConfigOption(description = "Output Directory (ignored for SDK ProjectType)", type = "Dir")
    private File                outputDir;

    @ConfigOption(description = "Maven Configurables", suboption = true)
    private MavenConfig         maven      = new MavenConfig();

    @ConfigOption(description = "Dry Run (show what would be done, but dont do it)")
    private boolean             dryRun     = false;

    private List<String>        fetchTargets;

    private File                rcDir;

    public FetcherConfig() {
        this.fetchTargets = new ArrayList<String>();
        // Setup complex default values
        this.rcDir = new File(SystemUtils.getUserHome(), ".sdkfido");
        this.workDir = new File(rcDir, "work");
        try {
            // Try to guess the android platforms directory.
            this.platformsDir = LocalAndroidPlatforms.findLocalJavaSdk();
        } catch (IOException e) {
            // Be quiet about inability to find android platforms directory,
            // as this value can be provided to the system by the user.
        }
        this.outputDir = new File(rcDir, "projects");
    }

    public void setFetchTargets(List<String> fetchTargets) {
        this.fetchTargets = fetchTargets;
    }

    @ConfigArguments(name = "fetch-targets")
    public void addFetchTarget(String target) {
        this.fetchTargets.add(target);
    }

    public List<String> getFetchTargets() {
        return fetchTargets;
    }

    public MavenConfig getMaven() {
        return maven;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public OutputProjectType getOutputType() {
        return outputType;
    }

    public File getPlatformsDir() {
        if (platformsDir == null) {
            try {
                this.platformsDir = LocalAndroidPlatforms.findLocalJavaSdk();
            } catch (IOException e) {
                LOG.log(Level.WARNING, e.getMessage());
            }
        }
        return platformsDir;
    }

    public File getRcDir() {
        return rcDir;
    }

    public File getWorkDir() {
        return workDir;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public void setMaven(MavenConfig mavenConfig) {
        this.maven = mavenConfig;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public void setOutputType(OutputProjectType outputProjectType) {
        this.outputType = outputProjectType;
    }

    public void setPlatformsDir(File platformsDir) {
        this.platformsDir = platformsDir;
    }

    public void setWorkDir(File workDir) {
        this.workDir = workDir;
    }
}

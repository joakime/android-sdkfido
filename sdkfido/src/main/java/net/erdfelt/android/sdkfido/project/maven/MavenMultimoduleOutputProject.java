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
package net.erdfelt.android.sdkfido.project.maven;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;
import net.erdfelt.android.sdkfido.local.JarListing;
import net.erdfelt.android.sdkfido.project.AbstractOutputProject;
import net.erdfelt.android.sdkfido.project.Dir;
import net.erdfelt.android.sdkfido.project.OutputProject;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;
import net.erdfelt.android.sdkfido.sdks.ProjectDependency;
import net.erdfelt.android.sdkfido.sdks.ProjectTemplate;

import org.apache.commons.io.FileUtils;

public class MavenMultimoduleOutputProject extends AbstractOutputProject implements OutputProject {
    private static final String             STUB_MODULE_ID = "android-stub";
    private Map<String, MavenOutputProject> modules;
    private String                          activeModule   = "_unset_";
    private MavenMultiParentGen             parentgen;
    private XmlBuildGen                     stubgen;
    private JarListing                      stublisting;
    private Dir                             stubDir;
    private Map<String, ProjectTemplate>    templates;

    public MavenMultimoduleOutputProject(File projectDir, FetchTarget target, Collection<ProjectTemplate> templateList) {
        this.baseDir = new Dir(projectDir, toBaseDirName(target));
        this.modules = new HashMap<String, MavenOutputProject>();
        this.templates = new HashMap<String, ProjectTemplate>();
        String parentArtifactId = MavenConstants.DEFAULT_ARTIFACTID;
        String parentVersion = target.getVersion().toString();
        String apilevel = target.getApilevel();

        parentgen = new MavenMultiParentGen(parentArtifactId, parentVersion, apilevel);
        stubgen = new MavenMultiStubGen(parentArtifactId, STUB_MODULE_ID, parentVersion, apilevel);

        // Establish Template Map
        for (ProjectTemplate pt : templateList) {
            pt.setVersion(parentVersion);
            pt.setParentVersion(parentVersion);
            pt.setParentArtifactId(parentArtifactId);
            pt.setApiLevel(apilevel);
            templates.put(pt.getId(), pt);
        }

        // Blow out Dependencies into Map
        for (String id : templates.keySet()) {
            ProjectTemplate proj = templates.get(id);
            if (!proj.getDependencies().isEmpty()) {
                for (ProjectDependency dep : proj.getDependencies()) {
                    dep.setProject(templates.get(dep.getRef()));
                }
            }
            templates.put(proj.getId(), proj);
        }
    }

    @Override
    public void startSubProject(String projectId) throws FetchException {
        this.activeModule = projectId;
        if (!modules.containsKey(this.activeModule)) {
            // Create new output module.
            File moduleDir = baseDir.getFile(projectId);
            ProjectTemplate template = templates.get(projectId);
            ProjectTemplateProjectGen gen = new ProjectTemplateProjectGen(template);
            MavenOutputProject module = new MavenOutputProject(moduleDir, gen);
            module.init();
            module.setCopierNarrowJar(stublisting);
            modules.put(this.activeModule, module);
        }
    }

    @Override
    public void copySource(File gitIncludeDir) throws FetchException {
        MavenOutputProject module = modules.get(activeModule);
        if (module == null) {
            throw new FetchException("Unable to find internal representation for module: " + activeModule);
        }

        module.copySource(gitIncludeDir);
    }

    @Override
    public void init() throws FetchException {
        baseDir.ensureEmpty();

        stubDir = baseDir.getSubDir(STUB_MODULE_ID);
        stubDir.ensureEmpty();

        if (androidStub != null) {
            try {
                FileUtils.copyFile(androidStub, stubDir.getFile("android-stub.jar"));
                this.stublisting = new JarListing(androidStub);
            } catch (IOException e) {
                throw new FetchException(
                        "Unable to narrow search tree by using listing of java files withou android stub jar file: "
                                + androidStub, e);
            }
        }
    }

    @Override
    public void close() throws FetchException {
        // Close all modules
        for (String moduleId : modules.keySet()) {
            MavenOutputProject module = modules.get(moduleId);

            if (module.getBuildGen() instanceof MavenMultiSubGen) {
                MavenMultiSubGen subgen = (MavenMultiSubGen) module.getBuildGen();

                // AAPT? Test for src/test/resources/AndroidManifest.xml
                File androidManifestXml = module.getBaseDir().getFile("src/main/resources/AndroidManifest.xml");
                subgen.setAndroidManifest(androidManifestXml);
            }

            module.close();
            parentgen.addModule(moduleId);
        }

        parentgen.addModule(STUB_MODULE_ID);
        stubgen.generate(stubDir.getFile("pom.xml"));
        parentgen.generate(baseDir.getFile("pom.xml"));
    }
}

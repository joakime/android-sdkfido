package net.erdfelt.android.sdkfido.project.maven;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;
import net.erdfelt.android.sdkfido.local.JarListing;
import net.erdfelt.android.sdkfido.project.AbstractOutputProject;
import net.erdfelt.android.sdkfido.project.Dir;
import net.erdfelt.android.sdkfido.project.OutputProject;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;

import org.apache.commons.io.FileUtils;

public class MavenMultimoduleOutputProject extends AbstractOutputProject implements OutputProject {
    private static final String             STUB_MODULE_ID = "android-stub";
    private Map<String, MavenOutputProject> modules;
    private String                          activeModule   = "_unset_";
    private String                          apilevel;
    private String                          parentArtifactId;
    private String                          parentVersion;
    private MavenMultiParentGen             parentgen;
    private XmlBuildGen                     stubgen;
    private JarListing                      stublisting;
    private Dir                             stubDir;

    public MavenMultimoduleOutputProject(File projectDir, FetchTarget target) {
        this.baseDir = new Dir(projectDir, toBaseDirName(target));
        this.modules = new HashMap<String, MavenOutputProject>();
        this.parentArtifactId = MavenConstants.DEFAULT_ARTIFACTID;
        this.parentVersion = target.getVersion().toString();
        this.apilevel = target.getApilevel();

        parentgen = new MavenMultiParentGen(parentArtifactId, parentVersion, apilevel);
        stubgen = new MavenMultiStubGen(parentArtifactId, STUB_MODULE_ID, parentVersion, apilevel);
    }

    @Override
    public void startSubProject(String projectId) throws FetchException {
        this.activeModule = projectId;
        if (!modules.containsKey(this.activeModule)) {
            // Create new output module.
            File moduleDir = baseDir.getFile(projectId);
            MavenMultiSubGen subgen = new MavenMultiSubGen(parentArtifactId, projectId, parentVersion, apilevel);
            MavenOutputProject module = new MavenOutputProject(moduleDir, subgen);
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

            MavenMultiSubGen subgen = (MavenMultiSubGen) module.getBuildGen();

            // AAPT? Test for src/test/resources/AndroidManifest.xml
            File androidManifestXml = module.getBaseDir().getFile("src/main/resources/AndroidManifest.xml");
            subgen.setAndroidManifest(androidManifestXml);

            module.close();
            parentgen.addModule(moduleId);
        }

        parentgen.addModule(STUB_MODULE_ID);
        stubgen.generate(stubDir.getFile("pom.xml"));
        parentgen.generate(baseDir.getFile("pom.xml"));
    }
}

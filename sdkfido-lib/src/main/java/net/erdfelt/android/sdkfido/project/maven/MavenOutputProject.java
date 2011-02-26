package net.erdfelt.android.sdkfido.project.maven;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;
import net.erdfelt.android.sdkfido.local.JarListing;
import net.erdfelt.android.sdkfido.project.AbstractOutputProject;
import net.erdfelt.android.sdkfido.project.AidlCompiler;
import net.erdfelt.android.sdkfido.project.Dir;
import net.erdfelt.android.sdkfido.project.JavaPathValidator;
import net.erdfelt.android.sdkfido.project.OutputProject;
import net.erdfelt.android.sdkfido.project.SourceCopier;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;

import org.apache.commons.io.FileUtils;

public class MavenOutputProject extends AbstractOutputProject implements OutputProject {
    public static final String  GROUPID    = "com.android.sdk";
    public static final String  ARTIFACTID = "android";

    private static final Logger LOG        = Logger.getLogger(MavenOutputProject.class.getName());
    private Dir                 sourceDir;
    private Dir                 resourceDir;
    private Dir                 outputDir;
    private SourceCopier        copier;
    private XmlBuildGen         buildgen;

    public MavenOutputProject(File projectDir, FetchTarget target) {
        baseDir = new Dir(projectDir, toBaseDirName(target));
        sourceDir = baseDir.getSubDir("src/main/java");
        resourceDir = baseDir.getSubDir("src/main/resources");
        outputDir = baseDir.getSubDir("target/classes");
        buildgen = new MavenPackageGen(target);
    }

    public MavenOutputProject(File moduleDir, XmlBuildGen buildgen) {
        this.baseDir = new Dir(moduleDir);
        this.sourceDir = baseDir.getSubDir("src/main/java");
        this.resourceDir = baseDir.getSubDir("src/main/resources");
        this.outputDir = baseDir.getSubDir("target/classes");
        this.buildgen = buildgen;
    }

    public XmlBuildGen getBuildgen() {
        return buildgen;
    }

    public void setBuildgen(XmlBuildGen buildgen) {
        this.buildgen = buildgen;
    }

    public void setCopierNarrowJar(JarListing jarlisting) {
        if (jarlisting != null) {
            this.copier.setNarrowSearchTo(jarlisting);
        }
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

        // AIDL Compile
        if (enableAidlCompilation) {
            outputDir.ensureEmpty();
            AidlCompiler aidl = new AidlCompiler(baseDir);
            try {
                aidl.compile(sourceDir, resourceDir, outputDir);
            } catch (IOException e) {
                throw new FetchException("AIDL Compilation Failure: " + e.getMessage(), e);
            }
        }

        // Create pom.xml
        buildgen.generate(baseDir.getFile("pom.xml"));
    }

    @Override
    public void copySource(File gitIncludeDir) throws FetchException {
        try {
            copier.copyTree(gitIncludeDir, sourceDir, resourceDir);
        } catch (IOException e) {
            throw new FetchException("Unable to copy source tree: " + gitIncludeDir, e);
        }
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

    @Override
    public String toString() {
        return String.format("Maven Output: %s", baseDir.getPath().getAbsolutePath());
    }
}

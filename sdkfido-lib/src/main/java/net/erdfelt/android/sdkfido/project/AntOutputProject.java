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

public class AntOutputProject extends AbstractOutputProject implements OutputProject {
    private static final Logger LOG = Logger.getLogger(AntOutputProject.class.getName());
    private Dir                 sourceDir;
    private SourceCopier        copier;
    private FetchTarget         target;

    public AntOutputProject(File projectsDir, FetchTarget target) {
        this.baseDir = new Dir(projectsDir, toBaseDirName(target));
        this.sourceDir = this.baseDir.getSubDir("src");
        this.target = target;
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
        props.put("TARGETID", target.getType().name().toLowerCase() + "-" + target.getId());
        FilteredFileUtil.copyWithExpansion("ant-build.xml", baseDir.getFile("build.xml"), props);
    }

    @Override
    public void copySource(File gitIncludeDir) throws FetchException {
        try {
            copier.copyTree(gitIncludeDir, sourceDir, sourceDir);
        } catch (IOException e) {
            throw new FetchException("Unable to copy source tree: " + gitIncludeDir, e);
        }
    }

    @Override
    public void init() throws FetchException {
        baseDir.ensureExists();
        sourceDir.ensureExists();

        try {
            this.copier = new SourceCopier(baseDir);
        } catch (IOException e) {
            throw new FetchException(e.getMessage(), e);
        }
        if (androidStub != null) {
            try {
                FileUtils.copyFile(androidStub, baseDir.getFile("android-stub.jar"));
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
        return String.format("Ant Output: %s", baseDir.getPath().getAbsolutePath());
    }
}
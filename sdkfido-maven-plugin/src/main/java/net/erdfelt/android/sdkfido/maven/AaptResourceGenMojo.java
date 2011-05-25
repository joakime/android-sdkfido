package net.erdfelt.android.sdkfido.maven;

import java.io.File;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Generate the R.java file from the /res/ files.
 * 
 * @goal aapt-resource-gen
 * @phase process-sources
 * @requiresProject true
 */
public class AaptResourceGenMojo extends AbstractSdkFidoMojo {
    /**
     * Output Directory.
     * 
     * @parameter expression="${project.build.directory}/generated-sources/aapt-resource-gen"
     * @required
     */
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File resDir = findResDir();
        if (resDir == null) {
            getLog().info("Skipping, no /res/ folder found in declared resource directories");
            return;
        }

        File manifestXml = findAndroidManifestXml();
        if (manifestXml == null) {
            getLog().info("Skipping, no AndroidManifest.xml found in declared resource directories");
            return;
        }

        getLog().debug(" /res/ Dir: " + resDir);
        getLog().debug("  Manifest: " + manifestXml);
        getLog().debug("Output Dir: " + outputDirectory);

        ensureDirExists(outputDirectory);

        Commandline cli = new Commandline();
        cli.setWorkingDirectory(project.getBasedir());
        cli.setExecutable("aapt");

        cli.createArg().setValue("package");
        cli.createArg().setValue("-m");
        cli.createArg().setValue("-J");
        cli.createArg().setFile(outputDirectory);
        cli.createArg().setValue("-M");
        cli.createArg().setFile(manifestXml);
        cli.createArg().setValue("-S");
        cli.createArg().setFile(resDir);
        cli.createArg().setValue("-I");
        // TODO: use android-sdk/android.jar ? (or stub?)
        File sourceDir = new File(project.getBuild().getSourceDirectory());
        cli.createArg().setFile(sourceDir);

        execCommandline(cli, "AAPT");
        
        project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    }

    @SuppressWarnings("unchecked")
    private File findAndroidManifestXml() {
        List<Resource> resources = project.getResources();
        File resourceDir, manifestXml;
        for (Resource resource : resources) {
            resourceDir = new File(resource.getDirectory());
            manifestXml = new File(resourceDir, "AndroidManifest.xml");
            if (manifestXml.exists() && manifestXml.isFile()) {
                return manifestXml;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private File findResDir() {
        List<Resource> resources = project.getResources();
        File resourceDir, resDir;
        for (Resource resource : resources) {
            resourceDir = new File(resource.getDirectory());
            resDir = new File(resourceDir, "res");
            if (resDir.exists() && resDir.isDirectory()) {
                return resDir;
            }
        }
        return null;
    }

}

package net.erdfelt.android.sdkfido.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Scan directory for AIDL files, and compile them.
 * 
 * @goal aidl
 * @phase process-sources
 * @requiresProject true
 */
public class AidlMojo extends AbstractMojo {
    /**
     * The maven project.
     * 
     * @parameter expression="${project.build.sourceDirectory}"
     */
    public File sourceDirectory;

    /**
     * Output Directory.
     * 
     * @parameter expression="${project.build.directory}/generated-sources/aidl"
     * @required
     */
    private File        outputDirectory;

    public void execute() throws MojoExecutionException {
        getLog().info("Source Dir: " + sourceDirectory);
        getLog().info("Output Dir: " + outputDirectory);
    }
}

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
package net.erdfelt.android.sdkfido.maven;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Scan directory for AIDL files, and compile them.
 * 
 * @goal aidl
 * @phase process-sources
 * @requiresProject true
 */
public class AidlMojo extends AbstractSdkFidoMojo {
    /**
     * The Source Directory to look for the AIDL files in.
     * 
     * @parameter expression="${project.build.sourceDirectory}"
     * @required
     */
    public File      sourceDirectory;

    /**
     * Output Directory.
     * 
     * @parameter expression="${project.build.directory}/generated-sources/aidl"
     * @required
     */
    private File     outputDirectory;

    /**
     * @parameter expression="${aidl.include}"
     */
    private String[] includes;

    /**
     * @parameter expression="${aidl.excludes}"
     */
    private String[] excludes;

    public void execute() throws MojoExecutionException {
        getLog().debug("Source Dir: " + sourceDirectory);
        getLog().debug("Output Dir: " + outputDirectory);

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.addDefaultExcludes();

        if (includes == null || includes.length == 0) {
            includes = new String[] { "**/*.aidl" };
        }
        scanner.setIncludes(includes);
        if (excludes != null && excludes.length > 0) {
            scanner.setExcludes(excludes);
        }
        getLog().debug("  Includes: " + toString(includes));
        getLog().debug("  Excludes: " + toString(excludes));
        scanner.setBasedir(sourceDirectory);
        scanner.scan();

        String aidls[] = scanner.getIncludedFiles();
        if (aidls == null || aidls.length <= 0) {
            getLog().info("No AIDLs found, skipping sdkfido:aidl goal");
            return;
        }

        ensureDirExists(outputDirectory);
        getLog().info("Compiling " + aidls.length + " AIDL file(s) ...");
        for (String aidl : aidls) {
            getLog().debug("AIDL: " + aidl);
            compileAidl(aidl);
        }

        getLog().info("Success: " + aidls.length + " AIDL(s) compiled into java source files");
        project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    }

    private void compileAidl(String aidl) throws MojoExecutionException {
        Commandline cli = new Commandline();
        cli.setWorkingDirectory(project.getBasedir());
        cli.setExecutable("aidl");

        String androidSdk = System.getenv("ANDROID_SDK");
        if (StringUtils.isNotBlank(androidSdk)) {
            // TODO: lookup framework.aidl in generation 2 format
            cli.createArg().setValue("-p" + androidSdk + OS("/tools/lib/framework.aidl"));
        }
        cli.createArg().setValue("-I" + sourceDirectory.getAbsolutePath());

        File aidlFile = new File(sourceDirectory, aidl);
        String javafilename = aidl.replaceFirst("\\.aidl$", ".java");
        File javaFile = new File(outputDirectory, javafilename);
        cli.createArg().setFile(aidlFile);
        cli.createArg().setFile(javaFile);

        execCommandline(cli, "AIDL");
    }
}

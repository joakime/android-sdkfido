package net.erdfelt.android.sdkfido.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Generate *.java files from found *.logtags files.
 * 
 * @goal logtags
 * @phase process-sources
 * @requiresProject true
 */
public class LogtagsMojo extends AbstractSdkFidoMojo {

    /**
     * Output Directory.
     * 
     * @parameter expression="${project.build.directory}/generated-sources/logtags"
     * @required
     */
    private File     outputDirectory;

    /**
     * @parameter expression="${logtags.include}"
     */
    private String[] includes;

    /**
     * @parameter expression="${logtags.excludes}"
     */
    private String[] excludes;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().debug("Output Dir: " + outputDirectory);

        String[] logtags = findLogtags();
        if (logtags == null || logtags.length <= 0) {
            getLog().info("No LOGTAGS found, skipping sdkfido:logtags goal");
            return;
        }

        ensureDirExists(outputDirectory);
        getLog().info("Compiling " + logtags.length + " AIDL file(s) ...");
        for (String logtag : logtags) {
            getLog().debug("Logtag: " + logtag);
            compileLogtag(logtag);
        }

        getLog().info("Success: " + logtags.length + " LOGTAG(s) compiled into java source files");
        project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    }

    private void compileLogtag(String logtag) {
        // TODO Auto-generated method stub

    }

    private String[] findLogtags() {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.addDefaultExcludes();

        List<String> patternIncludes = new ArrayList<String>();
        List<String> patternExcludes = new ArrayList<String>();

        if (includes == null || includes.length == 0) {
            includes = new String[] { "**/*.logtags" };
        }
        if (excludes == null) {
            excludes = new String[0];
        }

        patternExcludes.add("target/**");

        @SuppressWarnings("unchecked")
        List<Resource> resources = project.getResources();
        File resourceDir;
        for (Resource resource : resources) {
            resourceDir = new File(resource.getDirectory());
            for (String include : resource.getIncludes()) {
                patternIncludes.add(relativeToBasedir(resourceDir, '/' + include));
            }
            for (String exclude : resource.getExcludes()) {
                patternExcludes.add(relativeToBasedir(resourceDir, '/' + exclude));
            }
            for (String include : includes) {
                patternIncludes.add(relativeToBasedir(resourceDir, '/' + include));
            }
            for (String exclude : excludes) {
                patternExcludes.add(relativeToBasedir(resourceDir, '/' + exclude));
            }
        }

        scanner.setIncludes(patternIncludes.toArray(new String[0]));
        scanner.setExcludes(patternExcludes.toArray(new String[0]));
        getLog().debug("  Includes: " + toString(patternIncludes));
        getLog().debug("  Excludes: " + toString(patternExcludes));
        scanner.setBasedir(project.getBasedir());
        scanner.scan();

        return scanner.getIncludedFiles();
    }

    private String relativeToBasedir(File dir, String path) {
        File fulldir = new File(dir, OS(path));
        File basedir = project.getBasedir();
        String relativePath = basedir.toURI().relativize(fulldir.toURI()).toASCIIString();
        return OS(relativePath);
    }

}

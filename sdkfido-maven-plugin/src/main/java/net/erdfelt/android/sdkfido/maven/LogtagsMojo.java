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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Map<File, String[]> logtagMap = findLogtags();
        int tagCount = 0;

        for (String[] tags : logtagMap.values()) {
            tagCount += tags.length;
        }

        if (tagCount <= 0) {
            getLog().info("No LOGTAGS found, skipping sdkfido:logtags goal");
            return;
        }

        ensureDirExists(outputDirectory);
        getLog().info("Compiling " + tagCount + " AIDL file(s) ...");
        for (Map.Entry<File, String[]> entry : logtagMap.entrySet()) {
            File basedir = entry.getKey();
            for (String logtag : entry.getValue()) {
                getLog().debug("Logtag: " + logtag);
                compileLogtag(basedir, logtag);
            }
        }

        getLog().info("Success: " + tagCount + " LOGTAG(s) compiled into java source files");
        project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    }

    private void compileLogtag(File basedir, String logtagPath) throws MojoExecutionException {
        File tagfile = new File(basedir, logtagPath);
        try {
            LogTags tags = new LogTags(tagfile);
            StringBuilder javapath = new StringBuilder();
            javapath.append(tags.getPackageName().replace('.', File.separatorChar));
            javapath.append(File.separatorChar).append(tags.getClassName());
            javapath.append(".java");
            File javaFile = new File(outputDirectory, javapath.toString());
            ensureDirExists(javaFile.getParentFile());
            tags.writeJavaSource(javaFile);
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to compile logtag into java", e);
        }
    }

    private Map<File, String[]> findLogtags() {
        Map<File, String[]> tags = new HashMap<File, String[]>();

        if (includes == null || includes.length == 0) {
            includes = new String[] { "**/*.logtags" };
        }
        if (excludes == null) {
            excludes = new String[0];
        }

        getLog().debug("  Includes: " + toString(includes));
        getLog().debug("  Excludes: " + toString(excludes));

        @SuppressWarnings("unchecked")
        List<Resource> resources = project.getResources();
        File resourceDir;
        for (Resource resource : resources) {
            resourceDir = new File(resource.getDirectory());

            DirectoryScanner scanner = new DirectoryScanner();
            scanner.addDefaultExcludes();
            scanner.setIncludes(merged(includes, resource.getIncludes()));
            scanner.setExcludes(merged(excludes, resource.getExcludes()));
            scanner.setBasedir(resourceDir);
            scanner.scan();

            String hits[] = scanner.getIncludedFiles();
            if ((hits != null) && (hits.length > 0)) {
                tags.put(resourceDir, hits);
            }
        }

        return tags;
    }

    private String[] merged(String[] strArray, List<String> strList) {
        List<String> ret = new ArrayList<String>();
        for (String str : strList) {
            if (!ret.contains(str)) {
                ret.add(str);
            }
        }
        for (String str : strArray) {
            if (!ret.contains(str)) {
                ret.add(str);
            }
        }
        return ret.toArray(new String[0]);
    }
}

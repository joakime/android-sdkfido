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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.FetchTarget;
import net.erdfelt.android.sdkfido.project.FilteredFileUtil;
import net.erdfelt.android.sdkfido.project.OutputProjectException;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;

/**
 * Creates a maven <code>pom.xml</code> for a single maven build.
 * <p>
 * This generated pom.xml performs no compile, simply packages up the existing sources and attaches them to the active
 * project to allow them to be deployed and/or released.
 */
public class MavenPackageGen implements XmlBuildGen {
    private static final Logger LOG   = Logger.getLogger(MavenPackageGen.class.getName());
    private Map<String, String> props = new HashMap<String, String>();

    public MavenPackageGen(FetchTarget target) {
        props.put("GROUPID", MavenConstants.DEFAULT_GROUPID);
        props.put("ARTIFACTID", MavenConstants.DEFAULT_ARTIFACTID);
        props.put("VERSION", target.getVersion().toString());
    }

    @Override
    public void generate(File outputFile) throws OutputProjectException {
        LOG.info("Generating: " + outputFile);
        FilteredFileUtil.copyWithExpansion("maven-pom.xml", outputFile, props);
    }
}

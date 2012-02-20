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
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.project.FilteredFileUtil;
import net.erdfelt.android.sdkfido.project.OutputProjectException;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * Creates a maven <code>pom.xml</code> for a parent pom in a multi-module maven build.
 */
public class MavenMultiParentGen implements XmlBuildGen {
    private static final Logger LOG     = Logger.getLogger(MavenMultiParentGen.class.getName());
    private Map<String, String> props   = new HashMap<String, String>();
    private Set<String>         modules = new TreeSet<String>();

    public MavenMultiParentGen(String parentArtifactId, String parentVersion, String apilevel) {
        props.put("GROUPID", MavenConstants.DEFAULT_GROUPID);
        props.put("ARTIFACTID", parentArtifactId);
        props.put("VERSION", parentVersion);
        props.put("APILEVEL", apilevel);
    }

    @Override
    public void generate(File outputFile) throws OutputProjectException {
        LOG.info("Generating: " + outputFile);
        Document pom = FilteredFileUtil.loadExpandedXml("maven-multi-parent.xml", props);

        // Add modules section
        Element modulesNode = (Element) pom.selectSingleNode("/project/modules");
        if (modulesNode == null) {
            modulesNode = pom.getRootElement().addElement("modules");
        }

        for (String moduleId : modules) {
            modulesNode.addElement("module").addText(moduleId);
        }

        FilteredFileUtil.write(pom, outputFile);
    }

    public void addModule(String moduleId) {
        modules.add(moduleId);
    }
}

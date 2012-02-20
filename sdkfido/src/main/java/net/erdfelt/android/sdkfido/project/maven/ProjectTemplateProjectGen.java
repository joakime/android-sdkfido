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

import net.erdfelt.android.sdkfido.project.FilteredFileUtil;
import net.erdfelt.android.sdkfido.project.OutputProjectException;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;
import net.erdfelt.android.sdkfido.sdks.ProjectDependency;
import net.erdfelt.android.sdkfido.sdks.ProjectTemplate;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * Creates a maven <code>pom.xml</code> for a single maven build.
 * <p>
 * This generated pom.xml uses the template pom.xml found in the resources of this project.
 */
public class ProjectTemplateProjectGen implements XmlBuildGen {
    private static final Logger LOG   = Logger.getLogger(ProjectTemplateProjectGen.class.getName());
    private Map<String, String> props = new HashMap<String, String>();
    private ProjectTemplate     template;

    public ProjectTemplateProjectGen(ProjectTemplate template) {
        props.put("PARENT_GROUPID", template.getParentGroupId());
        props.put("PARENT_ARTIFACTID", template.getParentArtifactId());
        props.put("PARENT_VERSION", template.getParentVersion());
        props.put("GROUPID", template.getGroupId());
        props.put("ARTIFACTID", template.getArtifactId());
        props.put("VERSION", template.getVersion());
        props.put("APILEVEL", template.getApiLevel());
        this.template = template;
    }

    @Override
    public void generate(File outputFile) throws OutputProjectException {
        LOG.info("Generating: " + outputFile);

        Document pom = FilteredFileUtil.loadExpandedXml(template.getTemplateName(), props);

        if (template.hasDependencies()) {
            Element deps = (Element) pom.selectSingleNode("/project/dependencies");
            if (deps == null) {
                pom.getRootElement().addElement("dependencies");
            }

            for (ProjectDependency pd : template.getDependencies()) {
                ProjectTemplate proj = pd.getProject();
                Element dep = deps.addElement("dependency");
                dep.addElement("groupId").addText(proj.getGroupId());
                dep.addElement("artifactId").addText(proj.getArtifactId());
                dep.addElement("version").addText(proj.getVersion());
            }
        }

        FilteredFileUtil.write(pom, outputFile);
    }
}

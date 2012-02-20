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

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Creates a maven <code>pom.xml</code> for a sub-module in a multi-module maven build.
 * <p>
 * This generated pom.xml performs the following:
 * <ul>
 * <li>generate-sources
 * <ul>
 * <li>exec:exec [aapt] (optional if ${project.basedir}/src/main/resources/AndroidManifest.xml exists)</li>
 * </ul>
 * </li>
 * <li>generate-sources
 * <ul>
 * <li>sdkfido:aidl [compile-aidl] (optional if *.aidl files exist)</li>
 * </ul>
 * </li>
 * <li>compile</li>
 * <li>source:jar</li>
 * <li>javadoc:jar</li>
 * <li>package</li> </li>
 */
public class MavenMultiSubGen implements XmlBuildGen {
    private static final Logger LOG       = Logger.getLogger(MavenMultiSubGen.class.getName());
    private Map<String, String> props     = new HashMap<String, String>();
    private boolean             needsAapt = false;

    public MavenMultiSubGen(String parentArtifact, String subArtifact, String version, String apilevel) {
        props.put("GROUPID", MavenConstants.DEFAULT_GROUPID);
        props.put("PARENT_ARTIFACTID", parentArtifact);
        props.put("ARTIFACTID", subArtifact);
        props.put("VERSION", version);
        props.put("APILEVEL", apilevel);
    }

    @Override
    public void generate(File outputFile) throws OutputProjectException {
        LOG.info("Generating: " + outputFile);

        Document pom = FilteredFileUtil.loadExpandedXml("maven-multi-sub.xml", props);

        if (needsAapt) {
            Document aapt = FilteredFileUtil.loadExpandedXml("plugin-exec-aapt.xml", props);

            Element plugins = (Element) pom.selectSingleNode("/project/build/plugins");
            if (plugins == null) {
                plugins = pom.getRootElement().element("build").addElement("plugins");
            }

            plugins.add(aapt.getRootElement());
        }

        FilteredFileUtil.write(pom, outputFile);
    }

    public void setAndroidManifest(File androidManifestXml) throws OutputProjectException {
        if ((androidManifestXml == null) || (!androidManifestXml.exists())) {
            return;
        }

        SAXReader sax = null;
        try {
            sax = new SAXReader(false);
            Document doc = sax.read(androidManifestXml);

            String packageName = doc.getRootElement().attributeValue("package");
            packageName = packageName.replace('.', File.separatorChar);

            props.put("PACKAGEDIR", packageName);

            this.needsAapt = true;
        } catch (DocumentException e) {
            throw new OutputProjectException("Unable to parse AndroidManifest.xml", e);
        }
    }
}

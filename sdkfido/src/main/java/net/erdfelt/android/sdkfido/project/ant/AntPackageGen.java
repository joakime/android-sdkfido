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
package net.erdfelt.android.sdkfido.project.ant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.erdfelt.android.sdkfido.FetchTarget;
import net.erdfelt.android.sdkfido.project.FilteredFileUtil;
import net.erdfelt.android.sdkfido.project.OutputProjectException;
import net.erdfelt.android.sdkfido.project.XmlBuildGen;

/**
 * Generate an ant build.xml that does no compile step, just packages up the sources into a jar file.
 */
public class AntPackageGen implements XmlBuildGen {
    private Map<String, String> props;

    public AntPackageGen(FetchTarget target) {
        props = new HashMap<String, String>();
        props.put("TARGETID", target.getType().name().toLowerCase() + "-" + target.getId());
    }

    @Override
    public void generate(File outputFile) throws OutputProjectException {
        FilteredFileUtil.copyWithExpansion("ant-build.xml", outputFile, props);
    }
}

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

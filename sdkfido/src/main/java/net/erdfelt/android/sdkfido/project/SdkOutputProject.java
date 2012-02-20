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
package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.JarListing;

public class SdkOutputProject extends AbstractOutputProject implements OutputProject {
    private static final Logger LOG = Logger.getLogger(SdkOutputProject.class.getName());
    private Dir                 sourceDir;
    private SourceCopier        copier;

    public SdkOutputProject(AndroidPlatform platform) throws FetchException {
        this.baseDir = new Dir(platform.getDir());
        this.sourceDir = baseDir.getSubDir("sources");
    }

    @Override
    public String toString() {
        return String.format("Sdk Output: %s", sourceDir.getPath().getAbsolutePath());
    }

    @Override
    public void copySource(File gitIncludeDir) throws FetchException {
        try {
            copier.copyTree(gitIncludeDir, sourceDir, sourceDir);
        } catch (IOException e) {
            throw new FetchException("Unable to copy source tree: " + gitIncludeDir, e);
        }
    }

    @Override
    public void init() throws FetchException {
        sourceDir.ensureEmpty();
        try {
            this.copier = new SourceCopier(sourceDir);
        } catch (IOException e) {
            throw new FetchException(e.getMessage(), e);
        }
        if (androidStub != null) {
            try {
                // No need to copy the stub for Sdk Output Project Types (its already there!)
                copier.setNarrowSearchTo(new JarListing(androidStub));
            } catch (IOException e) {
                throw new FetchException(
                        "Unable to narrow search tree by using listing of java files withou android stub jar file: "
                                + androidStub, e);
            }
        }
    }

    @Override
    public void close() throws FetchException {
        copier.close();
        
        try {
            JavaPathValidator validator = new JavaPathValidator();
            int count = validator.validateSourceTree(sourceDir);
            LOG.info(String.format("Validation Success! (output source tree structure of %,d java files)%n", count));
        } catch (IOException e) {
            throw new FetchException(
                    "Java Source Validation Failed!  You might not have a valid set of source! (see console for details on why)",
                    e);
        }

        // TODO: AIDL Compile?
    }
}

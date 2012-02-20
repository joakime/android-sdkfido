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

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;

public abstract class AbstractOutputProject implements OutputProject {
    protected Dir     baseDir;
    protected String  androidStubApiLevel;
    protected File    androidStub;
    protected boolean enableAidlCompilation = false;

    @Override
    public void startSubProject(String projectId) throws FetchException {
        /* ignore */
    }

    @Override
    public Dir getBaseDir() {
        return this.baseDir;
    }
    
    @Override
    public void close() throws FetchException {
        /* ignore */
    }

    @Override
    public void setEnableAidlCompilation(boolean flag) {
        this.enableAidlCompilation = flag;
    }

    @Override
    public void setAndroidStub(String apilevel, File stubFile) {
        this.androidStubApiLevel = apilevel;
        this.androidStub = stubFile;
    }
    
    protected static StringBuilder toBaseDirName(FetchTarget target) {
        StringBuilder filename = new StringBuilder();
        filename.append("android-");
        switch (target.getType()) {
            case APILEVEL:
                filename.append("api-");
                break;
            case BRANCH:
            case TAG:
            case VERSION:
                filename.append(target.getType().name().toLowerCase()).append("-");
                break;
        }
        filename.append(target.getId());
        return filename;
    }
}

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

public interface OutputProject {
    public void startSubProject(String projectId) throws FetchException;

    public void copySource(File gitIncludeDir) throws FetchException;

    public Dir getBaseDir();

    public void init() throws FetchException;

    public void close() throws FetchException;

    public void setEnableAidlCompilation(boolean flag);

    public void setAndroidStub(String apilevel, File stubFile);
}

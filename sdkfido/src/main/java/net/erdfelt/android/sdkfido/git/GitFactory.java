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
package net.erdfelt.android.sdkfido.git;

import java.io.File;

import net.erdfelt.android.sdkfido.git.internal.InternalGit;

public final class GitFactory {
    private static GitMode    mode    = GitMode.INTERNAL;
    private static GitMirrors mirrors = new GitMirrors();

    public static IGit getGit(File dir) throws GitException {
        switch (mode) {
        case INTERNAL:
            return new InternalGit(dir, mirrors);
        default:
            throw new GitException("Sorry, only GitMode.INTERNAL currently supported.");
        }
    }

    public static void setMirrors(GitMirrors mirrors) {
        GitFactory.mirrors = mirrors;
    }

    public void setMode(GitMode mode) {
        if (mode == null) {
            GitFactory.mode = GitMode.INTERNAL;
        } else {
            GitFactory.mode = mode;
        }
    }
}

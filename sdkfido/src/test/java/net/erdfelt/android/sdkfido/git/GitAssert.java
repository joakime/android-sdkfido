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

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.toolchain.test.PathAssert;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.storage.file.FileRepository;
import org.junit.Assert;

public class GitAssert {

    public static void assertCheckoutRef(String repoId, String expectedGitRef, File repoDir) throws IOException {
        FileRepository repository = assertGitRepo(repoId, repoDir);
        Assert.assertThat(repoId + " - current branch ref", repository.getFullBranch(), is(expectedGitRef));
        Assert.assertThat(repoId + " - state", repository.getRepositoryState(), is(RepositoryState.SAFE));
    }

    public static FileRepository assertGitRepo(String repoId, File repoDir) {
        File gitDir = new File(repoDir, ".git");
        PathAssert.assertDirExists(repoId + " (.git)", gitDir);
        try {
            return new FileRepository(gitDir);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            Assert.fail(repoId + ": Not a git repository: " + repoDir);
        }
        // Not possible to reach this (as you will get a repo or a test failure)
        // Just putting it here to satisfy compiler.
        return null;
    }
}

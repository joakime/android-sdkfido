/*******************************************************************************
 * Copyright (c) Joakim Erdfelt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution.
 *
 *   The Eclipse Public License is available at 
 *   http://www.eclipse.org/legal/epl-v10.html
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

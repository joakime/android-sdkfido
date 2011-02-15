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
package net.erdfelt.android.sdkfido.sdks;

import static org.hamcrest.Matchers.*;

import java.io.IOException;

import net.erdfelt.android.sdkfido.logging.Logging;

import org.junit.Assert;
import org.junit.Test;

public class SourceOriginsTest {
    static {
        Logging.config();
    }

    @Test
    public void testLoadDefault() throws IOException, SourceOriginNotFoundException {
        SourceOrigins sdks = SourceOriginsLoader.load();
        Assert.assertNotNull("SourceOrigins should not be null", sdks);
        Assert.assertThat("source-origins.spec-version", sdks.getSpecVersion(), is(2));

        Assert.assertThat("source-origins.apilevels.size", sdks.getApilevels().size(), greaterThanOrEqualTo(9));
        Assert.assertThat("source-origins.tags.size", sdks.getTags().size(), greaterThanOrEqualTo(8));
        Assert.assertThat("source-origins.branches.size", sdks.getBranches().size(), greaterThanOrEqualTo(5));
        Assert.assertThat("source-origins.repos.size", sdks.getRepos().size(), greaterThanOrEqualTo(3));
        Assert.assertThat("source-origins.versions.size", sdks.getVersions().size(), greaterThanOrEqualTo(10));

        String prefix;

        prefix = "source-origins.apilevels[9]";
        ApiLevel api9 = sdks.getApiLevel("9");
        Assert.assertThat(prefix, api9, notNullValue());

        prefix = "source-origins.tags[android-sdk-2.0.1_r1]";
        Tag tag20 = sdks.getTag("android-sdk-2.0.1_r1");
        Assert.assertThat(prefix, tag20, notNullValue());

        prefix = "source-origins.branches[froyo-release]";
        Branch branchFroyo = sdks.getBranch("froyo-release");
        Assert.assertThat(prefix, branchFroyo, notNullValue());

        prefix = "source-origins.repos[base.git]";
        Repo repo = sdks.getRepo("git://android.git.kernel.org/platform/frameworks/base.git");
        Assert.assertThat(prefix, repo, notNullValue());
        Assert.assertThat(prefix + ".basedirs.size", repo.getBasedirs().size(), greaterThanOrEqualTo(10));
    }
}

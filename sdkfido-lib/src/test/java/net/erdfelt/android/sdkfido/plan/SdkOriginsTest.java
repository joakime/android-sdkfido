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
package net.erdfelt.android.sdkfido.plan;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Assert;
import org.junit.Test;

public class SdkOriginsTest {
    @Test
    public void testLoadBasicAsFile() throws IOException {
        File testJson = MavenTestingUtils.getTestResourceFile("config-tests/sdkfido-basic.xml");
        SdkOrigins sdk = SdkOrigins.load(testJson);
        Assert.assertThat("sdk.repos.size", sdk.getRepoKeys().size(), is(3));
        Assert.assertThat("sdk.base.uri", sdk.getRepoOriginUrl("base"),
                is("git://android.git.kernel.org/platform/frameworks/base.git"));
        Assert.assertThat("sdk.phone.uri", sdk.getRepoOriginUrl("phone"),
                is("git://android.git.kernel.org/platform/packages/apps/Phone.git"));
        Assert.assertThat("sdk.spec-version", sdk.getSpecVersion(), is(1));
        SdkVersion ver21 = sdk.getVersion("2.1");
        Assert.assertThat("sdk.version[2.1]", ver21, notNullValue());
        Assert.assertThat("sdk.version[2.1].codename", ver21.getCodename(), is("eclair"));
        Assert.assertThat("sdk.version[2.1].apilevel", ver21.getApilevel(), is(7));
    }

    @Test
    public void testLoadDefault() throws IOException {
        SdkOrigins sdk = SdkOrigins.load();
        Assert.assertThat("sdk.spec-version", sdk.getSpecVersion(), is(1));

        Collection<SdkRepo> repos = sdk.getRepos();
        Assert.assertThat("sdk.repos", repos, notNullValue());
        Assert.assertThat("sdk.repos.size", repos.size(), greaterThanOrEqualTo(1));

        SdkRepo repo = repos.iterator().next();
        Assert.assertThat("sdk.repos[0]", repo, notNullValue());
        Assert.assertThat("sdk.repos[0].id", repo.getId(), is("base"));
        Assert.assertThat("sdk.repos[0].uri", repo.getUri(),
                is("git://android.git.kernel.org/platform/frameworks/base.git"));

        Assert.assertThat("sdk.versions.count", sdk.getVersions().size(), greaterThanOrEqualTo(5));
        SdkVersion ver20 = sdk.getVersion("2.0");
        Assert.assertThat("sdk.version[2.0]", ver20, notNullValue());
        Assert.assertThat("sdk.version[2.0].codename", ver20.getCodename(), is("eclair"));
        Assert.assertThat("sdk.version[2.0].apilevel", ver20.getApilevel(), is(5));
        Assert.assertThat("sdk.version[2.0].id", ver20.getId(), is("2.0"));

        Collection<SdkTagRef> tags = ver20.getTagRefs();
        Assert.assertThat("sdk.version[2.0].tags", tags, notNullValue());
        Assert.assertThat("sdk.version[2.0].tags.size", tags.size(), is(1));
        SdkTagRef tag = tags.iterator().next();
        Assert.assertThat("sdk.version[2.0].tags[0]", tag, notNullValue());
        Assert.assertThat("sdk.version[2.0].tags[0].repoId", tag.getRepoId(), is("base"));
        Assert.assertThat("sdk.version[2.0].tags[0].name", tag.getName(), is("android-sdk-2.0.1_r1"));
        Assert.assertThat("sdk.version[2.0].tags[0].includes", tag.getIncludes(), notNullValue());
        Assert.assertThat("sdk.version[2.0].tags[0].includes.size", tag.getIncludes().size(), is(1));
        Assert.assertThat("sdk.version[2.0].tags[0].includes[0]", tag.getIncludes().get(0), is("core/java"));
    }
}

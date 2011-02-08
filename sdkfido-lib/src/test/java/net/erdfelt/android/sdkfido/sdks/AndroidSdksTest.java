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
import java.util.List;

import net.erdfelt.android.sdkfido.logging.Logging;

import org.junit.Assert;
import org.junit.Test;

public class AndroidSdksTest {
    static {
        Logging.config();
    }

    @Test
    public void testLoadDefault() throws IOException {
        AndroidSdks sdks = AndroidSdksLoader.load();
        Assert.assertNotNull("AndroidSdks should not be null", sdks);
        Assert.assertThat("sdks.spec-version", sdks.getSpecVersion(), is(1));

        String baserepoUrl = "git://android.git.kernel.org/platform/frameworks/base.git";
        String value = sdks.getProperty("base.repo");
        Assert.assertThat("sdk.property[base.repo]", value, is(baserepoUrl));

        Assert.assertThat("sdks.size", sdks.getCount(), greaterThanOrEqualTo(1));

        Sdk ver20 = sdks.getSdkByVersion("2.0");

        String prefix;

        prefix = "sdks.version[2.0]";
        Assert.assertThat(prefix, ver20, notNullValue());
        Assert.assertThat(prefix + ".codename", ver20.getCodename(), is("eclair"));
        Assert.assertThat(prefix + ".apilevel", ver20.getApilevel(), is(5));
        Assert.assertThat(prefix + ".version", ver20.getVersion(), is("2.0"));
        Assert.assertThat(prefix + ".id", ver20.getId(), is("android-sdk-2.0.1_r1"));

        prefix = prefix + ".repos";
        List<SdkRepo> repos = ver20.getRepos();
        Assert.assertThat(prefix, repos, notNullValue());
        Assert.assertThat(prefix + ".size", repos.size(), is(1));

        prefix = prefix + "[0]";
        SdkRepo repo = repos.get(0);
        Assert.assertThat(prefix, repo, notNullValue());
        Assert.assertThat(prefix + ".repoId", repo.getUrl(), is(baserepoUrl));
        Assert.assertThat(prefix + ".name", repo.getBranch(), is("android-sdk-2.0.1_r1"));

        prefix = prefix + ".includes";
        List<String> includes = repo.getIncludes();
        Assert.assertThat(prefix, includes, notNullValue());
        Assert.assertThat(prefix + ".size", includes.size(), is(1));
        Assert.assertThat(prefix + "[0]", includes.get(0), is("core/java"));
    }
}

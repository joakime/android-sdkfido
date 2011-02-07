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

import org.junit.Assert;
import org.junit.Test;

public class AndroidSdksTest {
    @Test
    public void testLoadDefault() throws IOException {
        AndroidSdks sdks = AndroidSdksLoader.load();
        Assert.assertThat("sdks.spec-version", sdks.getSpecVersion(), is(1));

        Assert.assertThat("sdks.size", sdks.getCount(), greaterThanOrEqualTo(1));

        Sdk ver20 = sdks.getSdkByVersion("2.0");
        
        Assert.assertThat("sdks.version[2.0]", ver20, notNullValue());
        Assert.assertThat("sdks.version[2.0].codename", ver20.getCodename(), is("eclair"));
        Assert.assertThat("sdks.version[2.0].apilevel", ver20.getApilevel(), is(5));
        Assert.assertThat("sdks.version[2.0].version", ver20.getVersion(), is("2.0"));
        Assert.assertThat("sdks.version[2.0].id", ver20.getId(), is("android-sdk-2.0.1_r1"));

        List<SdkRepo> repos = ver20.getRepos();
        Assert.assertThat("sdks.version[2.0].repos", repos, notNullValue());
        Assert.assertThat("sdks.version[2.0].repos.size", repos.size(), is(1));
        SdkRepo repo = repos.get(0);
        Assert.assertThat("sdks.version[2.0].repo[0]", repo, notNullValue());
        Assert.assertThat("sdks.version[2.0].repo[0].repoId", repo.getUrl(), is("${base.repo}"));
        Assert.assertThat("sdks.version[2.0].repo[0].name", repo.getBranch(), is("android-sdk-2.0.1_r1"));
    }
}

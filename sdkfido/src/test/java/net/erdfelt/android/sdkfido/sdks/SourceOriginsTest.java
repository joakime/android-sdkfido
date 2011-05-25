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

import net.erdfelt.android.sdkfido.FetchTarget;
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
    
    @Test
    public void testLoadDefaultProjects() throws IOException {
        SourceOrigins sdks = SourceOriginsLoader.load();
        Assert.assertNotNull("SourceOrigins should not be null", sdks);
        
        String prefix = "source-origins.projects[]";
        Assert.assertEquals(prefix + ".length", 3, sdks.getProjects().size());
        
        prefix = "source-origins.projects[base]";
        ProjectTemplate template = sdks.getProjectTemplate("base");
        Assert.assertNotNull(prefix + " should not be null", template);
        Assert.assertEquals(prefix + ".id", "base", template.getId());
        Assert.assertEquals(prefix + ".groupId", "com.android.sdk", template.getGroupId());
        Assert.assertEquals(prefix + ".artifactId", "base", template.getArtifactId());
        Assert.assertEquals(prefix + ".id", "maven-multi-android.xml", template.getTemplateName());
    }

    @Test
    public void testGetFetchTargets() throws IOException {
        SourceOrigins origins = SourceOriginsLoader.load();

        List<FetchTarget> targets = origins.getFetchTargets();
        Assert.assertThat("targets", targets, notNullValue());
        Assert.assertThat("targets.size", targets.size(), greaterThan(45));
    }

    @Test
    public void testGetApiLevelByVersion() throws IOException {
        SourceOrigins origins = SourceOriginsLoader.load();

        ApiLevel api = origins.getApiLevelByVersion(new Version("2.3.1"));
        Assert.assertThat("version 2.3.1 -> ApiLevel", api, notNullValue());
        Assert.assertThat("version 2.3.1 -> ApiLevel.version", api.getVersion(), is(new Version("2.3")));
        Assert.assertThat("version 2.3.1 -> ApiLevel.level", api.getLevel(), is("9"));
        Assert.assertThat("version 2.3.1 -> ApiLevel.codename", api.getCodename(), is("gingerbread"));
    }

    @Test
    public void testGetFetchTargetByApiLevel() throws IOException {
        SourceOrigins origins = SourceOriginsLoader.load();

        String id = "8";
        FetchTarget target = origins.getFetchTarget(id);
        String prefix = "target[" + id + "]";
        Assert.assertThat(prefix, target, notNullValue());
        Assert.assertThat(prefix + ": id", target.getId(), is(id));
        Assert.assertThat(prefix + ": type", target.getType(), is(SourceType.APILEVEL));
        Assert.assertThat(prefix + ": api level", target.getApilevel(), is(id));
        Assert.assertThat(prefix + ": version", target.getVersion(), is(new Version("2.2")));
        Assert.assertThat(prefix + ": code name", target.getCodename(), is("froyo"));
        Assert.assertThat(prefix + ": branch name", target.getBranchname(), is("android-sdk-2.2_r2"));
    }

    @Test
    public void testGetFetchTargetByTag() throws IOException {
        SourceOrigins origins = SourceOriginsLoader.load();

        String id = "android-2.3.1_r1";
        FetchTarget target = origins.getFetchTarget(id);
        String prefix = "target[" + id + "]";
        Assert.assertThat(prefix, target, notNullValue());
        Assert.assertThat(prefix + ": id", target.getId(), is(id));
        Assert.assertThat(prefix + ": type", target.getType(), is(SourceType.TAG));
        Assert.assertThat(prefix + ": api level", target.getApilevel(), is("9"));
        Assert.assertThat(prefix + ": version", target.getVersion(), is(new Version("2.3.1")));
        Assert.assertThat(prefix + ": code name", target.getCodename(), is("gingerbread"));
        Assert.assertThat(prefix + ": branch name", target.getBranchname(), is(id));
    }
}

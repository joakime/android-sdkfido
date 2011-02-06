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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.annotations.DigesterLoader;
import org.apache.commons.digester.annotations.DigesterLoaderBuilder;
import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;
import org.xml.sax.SAXException;

@ObjectCreate(pattern = "android-source")
public class SdkOrigins {
    private static final Logger     LOG         = Logger.getLogger(SdkOrigins.class.getName());
    @SetProperty(pattern = "android-source", attributeName = "spec-version")
    private int                     specVersion = 0;
    private Map<String, SdkRepo>    repoMap     = new HashMap<String, SdkRepo>();
    private Map<String, SdkVersion> versions    = new HashMap<String, SdkVersion>();

    public static SdkOrigins load() throws IOException {
        String resourcePath = "sdkfido-default.xml";
        URL url = SdkOrigins.class.getResource(resourcePath);
        if (url == null) {
            throw new FileNotFoundException("Unable to find resource: " + resourcePath);
        }

        DigesterLoader loader = DigesterLoaderBuilder.byDefaultFactories();
        Digester digester = loader.createDigester(SdkOrigins.class);

        SdkOrigins origins;
        try {
            origins = (SdkOrigins) digester.parse(url);
        } catch (SAXException e) {
            LOG.log(Level.WARNING, "Unable to load/parse URL: " + url, e);
            throw new IOException("Unable to load/parse URL: " + url, e);
        }

        return origins;
    }

    public static SdkOrigins load(File file) throws IOException {
        DigesterLoader loader = DigesterLoaderBuilder.byDefaultFactories();
        Digester digester = loader.createDigester(SdkOrigins.class);

        SdkOrigins origins;
        try {
            origins = (SdkOrigins) digester.parse(file);
        } catch (SAXException e) {
            LOG.log(Level.WARNING, "Unable to load/parse File: " + file, e);
            throw new IOException("Unable to load/parse File: " + file, e);
        }

        return origins;
    }

    public static SdkOrigins load(String originPath) throws IOException {
        URL url = SdkOrigins.class.getClassLoader().getResource(originPath);
        if (url == null) {
            throw new FileNotFoundException("Unable to find resource: " + originPath);
        }
        DigesterLoader loader = DigesterLoaderBuilder.byDefaultFactories();
        Digester digester = loader.createDigester(SdkOrigins.class);

        SdkOrigins origins;
        try {
            origins = (SdkOrigins) digester.parse(url);
        } catch (SAXException e) {
            LOG.log(Level.WARNING, "Unable to load/parse Resource: " + url, e);
            throw new IOException("Unable to load/parse Resource: " + url, e);
        }

        return origins;
    }

    public void setSpecVersion(int specVersion) {
        this.specVersion = specVersion;
    }

    public Set<String> getRepoKeys() {
        return Collections.unmodifiableSet(repoMap.keySet());
    }

    public String getRepoOriginUrl(String repoId) {
        return repoMap.get(repoId).getUri();
    }

    public int getSpecVersion() {
        return specVersion;
    }

    public SdkVersion getVersion(String versionId) {
        return versions.get(versionId);
    }

    public Set<String> getVersions() {
        return Collections.unmodifiableSet(versions.keySet());
    }

    @SetNext
    public void addVersion(SdkVersion version) {
        versions.put(version.getId(), version);
    }

    @SetNext
    public void addRepo(SdkRepo repo) {
        repoMap.put(repo.getId(), repo);
    }

    public Collection<SdkRepo> getRepos() {
        return repoMap.values();
    }
}

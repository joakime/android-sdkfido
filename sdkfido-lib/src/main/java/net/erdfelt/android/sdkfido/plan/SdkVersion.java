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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/version")
public class SdkVersion {
    @SetProperty(pattern = "android-source/version", attributeName = "id")
    private String                 id;

    @SetProperty(pattern = "android-source/version", attributeName = "codename")
    private String                 codename;

    @SetProperty(pattern = "android-source/version", attributeName = "apilevel")
    private int                    apilevel;

    private Map<String, SdkTagRef> tags = new HashMap<String, SdkTagRef>();

    @SetNext
    public void addTag(SdkTagRef tagref) {
        this.tags.put(tagref.getRepoId(), tagref);
    }

    public int getApilevel() {
        return apilevel;
    }

    public String getCodename() {
        return codename;
    }

    public String getId() {
        return id;
    }

    public SdkTagRef getTagId(String repoId) {
        return this.tags.get(repoId);
    }

    public Set<String> getTagRepoIds() {
        return Collections.unmodifiableSet(this.tags.keySet());
    }

    public void setApilevel(int apilevel) {
        this.apilevel = apilevel;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<SdkTagRef> getTagRefs() {
        return this.tags.values();
    }
}

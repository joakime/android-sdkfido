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
package net.erdfelt.android.sdkfido.sdks;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class VersionTree {
    private Version       version;
    private Set<Tag>      tags      = new TreeSet<Tag>();
    private Set<Branch>   branches  = new TreeSet<Branch>();
    private Set<ApiLevel> apis      = new TreeSet<ApiLevel>();
    private Set<String>   codenames = new TreeSet<String>(NewestCodenameSorter.INSTANCE);

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void addBranch(Branch branch) {
        this.branches.add(branch);
    }

    public void addApi(ApiLevel api) {
        this.apis.add(api);
    }

    public void addCodename(String codename) {
        this.codenames.add(codename);
    }

    public Set<String> getCodenames() {
        return codenames;
    }

    public void setCodenames(Set<String> codenames) {
        this.codenames = codenames;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Branch> getBranches() {
        return branches;
    }

    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public Set<ApiLevel> getApis() {
        return apis;
    }

    public void setApis(Set<ApiLevel> apis) {
        this.apis = apis;
    }

    public Tag getTopTag() {
        Iterator<Tag> iter = tags.iterator();
        if (iter.hasNext()) {
            return iter.next();
        }
        return null;
    }

    public ApiLevel getTopApiLevel() {
        Iterator<ApiLevel> iter = apis.iterator();
        if (iter.hasNext()) {
            return iter.next();
        }
        return null;
    }
}

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

import java.util.Set;
import java.util.TreeSet;

import net.erdfelt.android.sdkfido.util.CompareUtil;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/repos/repo")
public class Repo implements Comparable<Repo> {
    @SetProperty(pattern = "android-source/repos/repo")
    private String       url;
    private Set<BaseDir> basedirs = new TreeSet<BaseDir>();

    @SetNext
    public void addBasedir(BaseDir basedir) {
        this.basedirs.add(basedir);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Repo other = (Repo) obj;
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

    public Set<BaseDir> getBasedirs() {
        return basedirs;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    public void setBasedirs(Set<BaseDir> basedirs) {
        this.basedirs = basedirs;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Repo o) {
        return CompareUtil.compare(this.url, o.url);
    }
}

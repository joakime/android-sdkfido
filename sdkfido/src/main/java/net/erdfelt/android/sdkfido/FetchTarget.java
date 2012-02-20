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
package net.erdfelt.android.sdkfido;

import org.apache.commons.lang.StringUtils;

import net.erdfelt.android.sdkfido.sdks.SourceType;
import net.erdfelt.android.sdkfido.sdks.Version;

public class FetchTarget {
    private SourceType type;
    private String     id;
    private String     apilevel;
    private String     codename;
    private Version    version;
    private String     branchname;

    public FetchTarget(SourceType type, String id, String apilevel, String codename, Version version, String branchname) {
        super();
        this.type = type;
        this.id = id;
        this.apilevel = apilevel;
        this.codename = codename;
        this.version = version;
        this.branchname = branchname;
    }

    public boolean hasSCM() {
        return StringUtils.isNotBlank(this.branchname);
    }

    public String getApilevel() {
        return apilevel;
    }

    public String getCodename() {
        return codename;
    }

    public Version getVersion() {
        return version;
    }

    public String getBranchname() {
        return branchname;
    }

    public SourceType getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}

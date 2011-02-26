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

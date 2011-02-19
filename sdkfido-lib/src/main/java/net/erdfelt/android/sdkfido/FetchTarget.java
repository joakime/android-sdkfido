package net.erdfelt.android.sdkfido;

import net.erdfelt.android.sdkfido.sdks.SourceType;

public class FetchTarget {
    private SourceType type;
    private String     id;
    private String     apilevel;
    private String     codename;
    private String     version;
    private String     branchname;

    public FetchTarget(SourceType type, String id, String apilevel, String codename, String version, String branchname) {
        super();
        this.type = type;
        this.id = id;
        this.apilevel = apilevel;
        this.codename = codename;
        this.version = version;
        this.branchname = branchname;
    }

    public String getApilevel() {
        return apilevel;
    }

    public String getCodename() {
        return codename;
    }

    public String getVersion() {
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

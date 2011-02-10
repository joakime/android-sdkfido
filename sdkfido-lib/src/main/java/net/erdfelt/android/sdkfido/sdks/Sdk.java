package net.erdfelt.android.sdkfido.sdks;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "android-source/sdk")
public class Sdk {
    public static class VersionComparator implements Comparator<Sdk> {
        private Collator collator = Collator.getInstance();
        @Override
        public int compare(Sdk o1, Sdk o2) {
            CollationKey key1 = collator.getCollationKey(o1.version);
            CollationKey key2 = collator.getCollationKey(o2.version);
            return key1.compareTo(key2);
        }
    }

    @SetProperty(pattern = "android-source/sdk", attributeName = "id")
    private String        id;

    @SetProperty(pattern = "android-source/sdk", attributeName = "version")
    private String        version;

    @SetProperty(pattern = "android-source/sdk", attributeName = "codename")
    private String        codename;

    @SetProperty(pattern = "android-source/sdk", attributeName = "apilevel")
    private int           apilevel;

    private List<SdkRepo> repos    = new ArrayList<SdkRepo>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public int getApilevel() {
        return apilevel;
    }

    public void setApilevel(int apilevel) {
        this.apilevel = apilevel;
    }

    public List<SdkRepo> getRepos() {
        return repos;
    }

    public void setRepos(List<SdkRepo> repos) {
        this.repos = repos;
    }

    @SetNext
    public void addRepo(SdkRepo repo) {
        this.repos.add(repo);
    }
}

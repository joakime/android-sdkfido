package net.erdfelt.android.sdkfido.project;

import java.io.File;

import net.erdfelt.android.sdkfido.FetchException;
import net.erdfelt.android.sdkfido.FetchTarget;

public abstract class AbstractOutputProject implements OutputProject {
    protected Dir     baseDir;
    protected String  androidStubApiLevel;
    protected File    androidStub;
    protected boolean enableAidlCompilation = false;

    @Override
    public void startSubProject(String projectId) throws FetchException {
        /* ignore */
    }

    @Override
    public Dir getBaseDir() {
        return this.baseDir;
    }
    
    @Override
    public void close() throws FetchException {
        /* ignore */
    }

    @Override
    public void setEnableAidlCompilation(boolean flag) {
        this.enableAidlCompilation = flag;
    }

    @Override
    public void setAndroidStub(String apilevel, File stubFile) {
        this.androidStubApiLevel = apilevel;
        this.androidStub = stubFile;
    }
    
    protected static StringBuilder toBaseDirName(FetchTarget target) {
        StringBuilder filename = new StringBuilder();
        filename.append("android-");
        switch (target.getType()) {
            case APILEVEL:
                filename.append("api-");
                break;
            case BRANCH:
            case TAG:
            case VERSION:
                filename.append(target.getType().name().toLowerCase()).append("-");
                break;
        }
        filename.append(target.getId());
        return filename;
    }
}

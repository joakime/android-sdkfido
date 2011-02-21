package net.erdfelt.android.sdkfido.project;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.FetchTarget;

public abstract class AbstractOutputProject implements OutputProject {
    protected Dir     baseDir;
    protected Dir     sourceDir;
    protected Dir     resourceDir;
    protected Dir     outputDir;
    protected String  androidStubApiLevel;
    protected File    androidStub;
    protected boolean enableAidlCompilation = false;

    @Override
    public void startSubProject(String projectId) throws IOException {
        /* ignore */
    }

    @Override
    public Dir getBaseDir() {
        return this.baseDir;
    }
    
    @Override
    public Dir getOutputDir() {
        return this.outputDir;
    }
    
    @Override
    public Dir getResourceDir() {
        return this.resourceDir;
    }
    
    @Override
    public Dir getSourceDir() {
        return this.sourceDir;
    }

    @Override
    public void close() throws IOException {
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

    protected StringBuilder toBaseDirName(FetchTarget target) {
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

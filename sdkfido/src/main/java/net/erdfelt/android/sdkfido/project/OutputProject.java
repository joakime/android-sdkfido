package net.erdfelt.android.sdkfido.project;

import java.io.File;

import net.erdfelt.android.sdkfido.FetchException;

public interface OutputProject {
    public void startSubProject(String projectId) throws FetchException;

    public void copySource(File gitIncludeDir) throws FetchException;

    public Dir getBaseDir();

    public void init() throws FetchException;

    public void close() throws FetchException;

    public void setEnableAidlCompilation(boolean flag);

    public void setAndroidStub(String apilevel, File stubFile);
}

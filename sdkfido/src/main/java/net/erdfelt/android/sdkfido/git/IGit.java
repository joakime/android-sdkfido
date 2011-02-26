package net.erdfelt.android.sdkfido.git;

import java.io.File;

public interface IGit {
    static final String REMOTE_NAME = "origin";

    public void checkoutBranch(String branchName) throws GitException;

    public void clone(String url) throws GitException;

    /**
     * Returns if the repository exists (or not)
     * 
     * @return true if repository exists.
     */
    public boolean exists();

    public String getCurrentBranch() throws GitException;

    public void pullRemote() throws GitException;

    public File getDir();

}
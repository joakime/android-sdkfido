package net.erdfelt.android.sdkfido.git;

import java.io.File;

import net.erdfelt.android.sdkfido.git.internal.InternalGit;

public final class GitFactory {
    private static GitMode    mode    = GitMode.INTERNAL;
    private static GitMirrors mirrors = new GitMirrors();

    public static IGit getGit(File dir) throws GitException {
        switch (mode) {
        case INTERNAL:
            return new InternalGit(dir, mirrors);
        default:
            throw new GitException("Sorry, only GitMode.INTERNAL currently supported.");
        }
    }

    public static void setMirrors(GitMirrors mirrors) {
        GitFactory.mirrors = mirrors;
    }

    public void setMode(GitMode mode) {
        if (mode == null) {
            GitFactory.mode = GitMode.INTERNAL;
        } else {
            GitFactory.mode = mode;
        }
    }
}

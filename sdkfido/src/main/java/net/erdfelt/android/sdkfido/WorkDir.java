package net.erdfelt.android.sdkfido;

import java.io.File;

import net.erdfelt.android.sdkfido.git.GitException;
import net.erdfelt.android.sdkfido.git.GitFactory;
import net.erdfelt.android.sdkfido.git.IGit;

public class WorkDir {
    private File baseDir;

    public WorkDir(File dir) {
        this.baseDir = dir;
    }

    public IGit getGitRepo(String url) throws GitException {
        String safeFilename = toSafeFilename(url);
        File repoDir = new File(baseDir, safeFilename);
        return GitFactory.getGit(repoDir);
    }

    /**
     * Clean up a raw string to fit within the rules of a valid filename on all OS's.
     * 
     * @param raw
     *            the raw string to cleanup
     * @return the cleaned up version
     */
    public static String toSafeFilename(String raw) {
        char[] filename = raw.toCharArray();
        int len = filename.length;
        for (int i = 0; i < len; i++) {
            switch (filename[i]) {
            case '/':
            case '\\':
            case '?':
            case '%':
            case '*':
            case ':':
            case '|':
            case '"':
            case '<':
            case '>':
            case ' ':
                filename[i] = '_';
                break;
            }
        }
        return String.valueOf(filename);
    }

}

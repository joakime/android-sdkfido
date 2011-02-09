package net.erdfelt.android.sdkfido;

import java.io.File;
import java.io.IOException;

import net.erdfelt.android.sdkfido.git.GitRepo;

public class WorkDir {
    private File baseDir;

    public WorkDir(File dir) {
        this.baseDir = dir;
    }

    public GitRepo getGitRepo(String url) throws IOException {
        String safeFilename = toSafeFilename(url);
        File repoDir = new File(baseDir, safeFilename);
        return new GitRepo(repoDir);
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

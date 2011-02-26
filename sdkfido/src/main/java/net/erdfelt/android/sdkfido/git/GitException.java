package net.erdfelt.android.sdkfido.git;

import net.erdfelt.android.sdkfido.FetchException;

public class GitException extends FetchException {
    private static final long serialVersionUID = -7038654976200660493L;

    public GitException(String message, Throwable cause) {
        super(message, cause);
    }

    public GitException(String message) {
        super(message);
    }
}

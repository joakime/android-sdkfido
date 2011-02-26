package net.erdfelt.android.sdkfido.project;

import net.erdfelt.android.sdkfido.FetchException;

public class OutputProjectException extends FetchException {
    private static final long serialVersionUID = 1796533661324954069L;

    public OutputProjectException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public OutputProjectException(String msg) {
        super(msg);
    }

    public OutputProjectException(Throwable cause) {
        super(cause);
    }
}

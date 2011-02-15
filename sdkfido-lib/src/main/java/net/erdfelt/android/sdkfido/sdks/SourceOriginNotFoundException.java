package net.erdfelt.android.sdkfido.sdks;

public class SourceOriginNotFoundException extends Exception {
    private static final long serialVersionUID = -263455093800271653L;

    public SourceOriginNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SourceOriginNotFoundException(String msg) {
        super(msg);
    }
}

package net.erdfelt.android.sdkfido;

public class FetchException extends Exception {
    private static final long serialVersionUID = 1250642681050823110L;

    public FetchException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public FetchException(String arg0) {
        super(arg0);
    }

    public FetchException(Throwable arg0) {
        super(arg0);
    }
}

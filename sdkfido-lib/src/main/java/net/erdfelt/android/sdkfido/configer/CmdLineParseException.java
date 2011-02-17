package net.erdfelt.android.sdkfido.configer;

public class CmdLineParseException extends Exception {
    private static final long serialVersionUID = -5585807464722440218L;

    public CmdLineParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CmdLineParseException(String message) {
        super(message);
    }
}

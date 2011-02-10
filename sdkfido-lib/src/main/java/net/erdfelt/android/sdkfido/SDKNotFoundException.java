package net.erdfelt.android.sdkfido;

import java.io.IOException;

public class SDKNotFoundException extends IOException {
    private static final long serialVersionUID = 1L;

    public SDKNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKNotFoundException(String message) {
        super(message);
    }
}

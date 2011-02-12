package net.erdfelt.android.sdkfido.local;

import java.io.IOException;

public class AndroidPlatformNotFoundException extends IOException {
    private static final long serialVersionUID = 1L;

    public AndroidPlatformNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AndroidPlatformNotFoundException(String message) {
        super(message);
    }
}

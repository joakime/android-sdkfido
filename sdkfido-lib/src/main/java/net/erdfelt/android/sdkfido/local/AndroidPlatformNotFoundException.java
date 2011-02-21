package net.erdfelt.android.sdkfido.local;

import net.erdfelt.android.sdkfido.FetchException;

public class AndroidPlatformNotFoundException extends FetchException {
    private static final long serialVersionUID = 7075630317209742177L;

    public AndroidPlatformNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AndroidPlatformNotFoundException(String message) {
        super(message);
    }
}

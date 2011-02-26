package net.erdfelt.android.sdkfido.configer;

public class InvalidValueException extends RuntimeException {
    private static final long serialVersionUID = -3318038377899310587L;

    public InvalidValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidValueException(String message) {
        super(message);
    }
}

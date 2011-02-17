package net.erdfelt.android.sdkfido.configer;

public abstract class ValueValidator<T> {
    public abstract void validate(T value) throws InvalidValueException;
}

package net.erdfelt.android.sdkfido;

public interface Task {
    String getName();

    String getStatusMessage();

    int getProgressMax();

    int getProgressCurrent();
}

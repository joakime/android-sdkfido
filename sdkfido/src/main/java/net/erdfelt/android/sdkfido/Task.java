package net.erdfelt.android.sdkfido;

public interface Task {
    String getName();

    void run(TaskQueue tasks) throws Throwable;
}

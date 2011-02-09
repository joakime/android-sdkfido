package net.erdfelt.android.sdkfido;

public interface Task {
    String getName();

    void run(TaskListener tasklistener, TaskQueue tasks) throws Throwable;
}

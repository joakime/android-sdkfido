package net.erdfelt.android.sdkfido;

public interface TaskListener {
    public void update(Task task, int progress, int progressMax, String status);

    public void taskFailed(Task task, Throwable t);

    public void taskStart(Task task);

    public void taskFinish(Task task);

    public void tasksStarted();

    public void tasksFinished();
}

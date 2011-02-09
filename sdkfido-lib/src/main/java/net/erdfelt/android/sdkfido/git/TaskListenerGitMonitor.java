package net.erdfelt.android.sdkfido.git;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;

import org.eclipse.jgit.lib.ProgressMonitor;

public class TaskListenerGitMonitor implements ProgressMonitor {
    private Task         task;
    private TaskListener listener;
    private int          progress;
    private int          progressMax;
    private String       status;

    public TaskListenerGitMonitor(Task task, TaskListener listener) {
        this.task = task;
        this.listener = listener;

        progress = 0;
        progressMax = -1;
    }

    @Override
    public void start(int totalTasks) {
        /* ignore */
    }

    @Override
    public void beginTask(String title, int totalWork) {
        if (progressMax < 0) {
            progressMax = totalWork;
        } else {
            progressMax += totalWork;
        }
        status = title;
        listener.update(task, progress, progressMax, status);
    }

    @Override
    public void update(int completed) {
        if (completed != UNKNOWN) {
            progress += completed;
            listener.update(task, progress, progressMax, status);
        }
    }

    @Override
    public void endTask() {
        status = "Completed";
        progress = progressMax;

        listener.update(task, progress, progressMax, status);
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}

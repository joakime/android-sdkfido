package net.erdfelt.android.sdkfido.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;

public class LoggingTaskListener implements TaskListener {
    private static final Logger LOG = Logger.getLogger(LoggingTaskListener.class.getName());

    @Override
    public void update(Task task, int progress, int progressMax, String status) {
        LOG.info("update(" + task + ", " + progress + ", " + progressMax + ", " + status + ")");
    }

    @Override
    public void taskFailed(Task task, Throwable t) {
        LOG.log(Level.WARNING, "taskFailed(" + task + ", " + t.getClass().getName() + ")", t);
    }

    @Override
    public void taskStart(Task task) {
        LOG.info("taskStart(" + task + ")");
    }

    @Override
    public void taskFinish(Task task) {
        LOG.info("taskFinish(" + task + ")");
    }

    @Override
    public void tasksStarted() {
        LOG.info("tasksStarted()");
    }

    @Override
    public void tasksFinished() {
        LOG.info("tasksFinished()");
    }
}

package net.erdfelt.android.sdkfido;

import java.io.PrintStream;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;

public class ConsoleTaskListener implements TaskListener {
    private PrintStream out = System.out;
    
    @Override
    public void update(Task task, int progress, int progressMax, String status) {
        out.printf("Task.update - (%s) %d/%d - %s%n", task.getName(), progress, progressMax, status);
    }

    @Override
    public void taskFailed(Task task, Throwable t) {
        out.printf("Task.failed - (%s) %s%n", task.getName(), t.getMessage());
        t.printStackTrace(out);
    }

    @Override
    public void taskStart(Task task) {
        out.printf("Task.start - (%s)%n", task.getName());
    }

    @Override
    public void taskFinish(Task task) {
        out.printf("Task.finish - (%s)%n", task.getName());
    }

    @Override
    public void tasksStarted() {
        out.printf("Tasks - Started%n");
    }

    @Override
    public void tasksFinished() {
        out.printf("Tasks - Finished%n");
    }
}

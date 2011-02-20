package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.project.OutputProject;

public class ProjectCloseTask implements Task {
    private OutputProject project;

    public ProjectCloseTask(OutputProject project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "Close Project: " + project;
    }

    @Override
    public void run(TaskListener tasklistener, TaskQueue tasks) throws Throwable {
        project.close();
    }
}

package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.project.Project;

public class InitProjectTask implements Task {
    private Project project;

    public InitProjectTask(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "Init Project: " + project.getId();
    }

    @Override
    public void run(TaskListener listener, TaskQueue tasks) {
        project.create();
    }
}

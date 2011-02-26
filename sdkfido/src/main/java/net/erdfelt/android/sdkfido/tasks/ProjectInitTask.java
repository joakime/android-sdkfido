package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.project.OutputProject;

public class ProjectInitTask implements Task {
    private OutputProject project;
    
    public ProjectInitTask(OutputProject project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "Init Project: " + project;
    }

    @Override
    public void run(TaskQueue tasks) throws Throwable {
        project.init();
    }
}

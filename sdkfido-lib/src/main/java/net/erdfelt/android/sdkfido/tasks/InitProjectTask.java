package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.project.Project;
import net.erdfelt.android.sdkfido.project.SourceCopier;

public class InitProjectTask implements Task {
    private Project      project;
    private SourceCopier copier;

    public InitProjectTask(Project project, SourceCopier copier) {
        this.project = project;
        this.copier = copier;
    }

    @Override
    public String getName() {
        return "Init Project: " + project.getBaseDir();
    }

    @Override
    public void run(TaskListener listener, TaskQueue tasks) throws Throwable {
        project.create();
        project.cleanSourceTree();
        copier.init();
    }
}

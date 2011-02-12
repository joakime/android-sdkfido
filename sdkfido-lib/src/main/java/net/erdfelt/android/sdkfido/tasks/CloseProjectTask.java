package net.erdfelt.android.sdkfido.tasks;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.project.SourceCopier;

public class CloseProjectTask implements Task {
    private SourceCopier copier;

    public CloseProjectTask(SourceCopier copier) {
        this.copier = copier;
    }

    @Override
    public String getName() {
        return "Close Active Project Copier";
    }

    @Override
    public void run(TaskListener tasklistener, TaskQueue tasks) throws Throwable {
        copier.close();
    }
}

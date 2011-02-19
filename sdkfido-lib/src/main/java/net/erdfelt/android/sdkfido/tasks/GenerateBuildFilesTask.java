package net.erdfelt.android.sdkfido.tasks;

import java.util.HashMap;
import java.util.Map;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.project.Project;

public class GenerateBuildFilesTask implements Task {
    private Project project;

    public GenerateBuildFilesTask(Project project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return "Generate Build Files: " + this.project.getId();
    }

    @Override
    public void run(TaskListener listener, TaskQueue tasks) {
        Map<String,String> props = new HashMap<String,String>();
        props.put("GROUPID", "com.android.sdkfido");
        props.put("ARTIFACTID", "android");
        // TODO: props.put("VERSION", this.sdk.getVersion());
        this.project.createBuildFiles(props);
    }

}

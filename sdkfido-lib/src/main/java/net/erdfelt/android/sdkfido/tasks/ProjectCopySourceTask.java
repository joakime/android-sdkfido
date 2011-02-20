package net.erdfelt.android.sdkfido.tasks;

import java.io.File;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.git.IGit;
import net.erdfelt.android.sdkfido.project.OutputProject;
import net.erdfelt.android.sdkfido.sdks.BaseDir;
import net.erdfelt.android.sdkfido.sdks.Repo;

import org.apache.commons.io.FilenameUtils;

public class ProjectCopySourceTask implements Task {
    private IGit          git;
    private Repo          repo;
    private OutputProject project;

    public ProjectCopySourceTask(IGit git, Repo repo, OutputProject project) {
        super();
        this.git = git;
        this.repo = repo;
        this.project = project;
    }

    @Override
    public String getName() {
        return "Copy Sources: " + project;
    }

    @Override
    public void run(TaskQueue tasks) throws Throwable {
        for (BaseDir basedir : repo.getBasedirs()) {
            String include = basedir.getPath();
            File sourceDir = new File(git.getDir(), FilenameUtils.separatorsToSystem(include));
            project.startSubProject(basedir.getProject());
            project.copySource(sourceDir);
        }
    }
}

package net.erdfelt.android.sdkfido.tasks;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskListener;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.WorkDir;
import net.erdfelt.android.sdkfido.git.GitRepo;
import net.erdfelt.android.sdkfido.local.AndroidPlatform;
import net.erdfelt.android.sdkfido.local.JarListing;
import net.erdfelt.android.sdkfido.project.Project;
import net.erdfelt.android.sdkfido.sdks.SdkRepo;

public class CopyGitSourceToProjectTask implements Task {
    private WorkDir         workdir;
    private SdkRepo         repo;
    private AndroidPlatform platform;
    private Project         project;

    public CopyGitSourceToProjectTask(WorkDir workdir, SdkRepo repo, AndroidPlatform platform, Project project) {
        this.workdir = workdir;
        this.repo = repo;
        this.platform = platform;
        this.project = project;
    }

    @Override
    public String getName() {
        return "Copy Sources";
    }

    @Override
    public void run(TaskListener listener, TaskQueue tasks) throws Throwable {
        JarListing listing = platform.getAndroidJarListing();
        GitRepo git = workdir.getGitRepo(repo.getUrl());
        for (String include : repo.getIncludes()) {
            File searchDir = new File(git.getDir(), toOS(include));
            File searchFile, destFile;
            for (String classentry : listing.getClassList()) {
                searchFile = new File(searchDir, toOS(classentry));
                if (searchFile.exists()) {
                    destFile = project.getSrcJava(classentry);
                    FileUtils.copyFile(searchFile, destFile);
                }
            }
        }
    }

    private String toOS(String path) {
        return FilenameUtils.separatorsToSystem(path);
    }
}

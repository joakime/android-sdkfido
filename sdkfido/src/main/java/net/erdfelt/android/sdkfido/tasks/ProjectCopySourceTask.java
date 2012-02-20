/*******************************************************************************
 *    Copyright 2012 - Joakim Erdfelt
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
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

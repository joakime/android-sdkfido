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

import net.erdfelt.android.sdkfido.Task;
import net.erdfelt.android.sdkfido.TaskQueue;
import net.erdfelt.android.sdkfido.git.IGit;

import org.apache.commons.lang.StringUtils;

public class GitSwitchBranchTask implements Task {
    private IGit   git;
    private String branch;

    public GitSwitchBranchTask(IGit git, String branch) {
        this.git = git;
        this.branch = branch;
        if (StringUtils.isBlank(branch)) {
            throw new IllegalArgumentException("Branch Name cannot be blank");
        }
    }

    @Override
    public String getName() {
        return "Git Switch Branch: " + branch;
    }

    @Override
    public void run(TaskQueue tasks) throws Throwable {
        git.checkoutBranch(branch);
    }
}

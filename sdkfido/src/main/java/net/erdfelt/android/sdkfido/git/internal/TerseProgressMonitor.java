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
package net.erdfelt.android.sdkfido.git.internal;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.lib.ProgressMonitor;

public class TerseProgressMonitor implements ProgressMonitor {
    private static final int  PROGRESS_BAR_LEN  = 72;
    private static final char PROGRESS_BAR_HASH = '-';
    private static final char PROGRESS_BAR_TAIL = '|';
    private boolean           output            = false;
    private String            title;
    private int               totalWork;
    private int               completed;
    private int               currentBar;

    @Override
    public void beginTask(String title, int totalWork) {
        String contents = String.format("%s - (%s)", title, totalWork);
        if (this.output) {
            System.out.println(PROGRESS_BAR_TAIL);
        }
        System.out.print(StringUtils.rightPad(contents, PROGRESS_BAR_LEN, ' '));
        System.out.println(PROGRESS_BAR_TAIL);
        this.title = title;
        this.completed = 0;
        this.currentBar = 0;
        this.totalWork = totalWork;
    }

    @Override
    public void endTask() {
        if (this.output) {
            System.out.println(PROGRESS_BAR_TAIL);
        }
        this.output = false;
        this.title = null;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void start(int totalTasks) {
        /* nothing to do here */
    }

    @Override
    public void update(int amountWorked) {
        if (title == null) {
            return;
        }

        completed += amountWorked;

        if (completed >= totalWork) {
            output = true;
            int barNow = PROGRESS_BAR_LEN;
            int diff = barNow - currentBar;
            for (int i = 0; i < diff; i++) {
                System.out.print(PROGRESS_BAR_HASH);
            }
            currentBar = barNow;
            return;
        }

        if ((completed == UNKNOWN) || (totalWork == UNKNOWN)) {
            return;
        }

        double percentage = (double) completed / (double) totalWork;
        int barNow = (int) (PROGRESS_BAR_LEN * percentage);
        if (barNow > currentBar) {
            output = true;
            int diff = barNow - currentBar;
            for (int i = 0; i < diff; i++) {
                System.out.print(PROGRESS_BAR_HASH);
            }
            currentBar = barNow;
        }
    }
}

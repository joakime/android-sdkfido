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
package net.erdfelt.android.sdkfido;

import java.util.Collection;
import java.util.LinkedList;

public class TaskQueue extends LinkedList<Task> {
    private static final long serialVersionUID = -3890246296713749227L;

    public void insertAtHead(Collection<Task> tasks) {
        addAll(0, tasks);
    }

    public void insertAtHead(Task... tasks) {
        for (int i = 0; i < tasks.length; i++) {
            add(i, tasks[i]);
        }
    }

    private Task current;

    public Task getCurrentTask() {
        return this.current;
    }

    public Task nextTask() {
        this.current = remove();
        return this.current;
    }

    public boolean hasTask() {
        return (this.peek() != null);
    }
}

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

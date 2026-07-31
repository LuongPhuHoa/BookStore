package org.example.springcorelab;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class TaskCoordinator {

    private final ObjectProvider<PrototypeTask> taskProvider;

    public TaskCoordinator(ObjectProvider<PrototypeTask> taskProvider) {
        this.taskProvider = taskProvider;
    }

    public PrototypeTask createTask() {
        return taskProvider.getObject();
    }
}

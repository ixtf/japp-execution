package org.jzb.execution.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class TasksUpdateCommand implements Serializable {
    @NotNull
    @Size(min = 1)
    List<EntityDTO> tasks;

    public List<EntityDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<EntityDTO> tasks) {
        this.tasks = tasks;
    }
}

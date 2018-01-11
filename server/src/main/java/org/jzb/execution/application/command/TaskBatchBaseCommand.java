package org.jzb.execution.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-4-15.
 */
public class TaskBatchBaseCommand implements Serializable {
    @Size(min = 1)
    @NotNull
    protected List<EntityDTO> tasks;

    public List<EntityDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<EntityDTO> tasks) {
        this.tasks = tasks;
    }
}

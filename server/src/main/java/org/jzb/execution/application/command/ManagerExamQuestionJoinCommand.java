package org.jzb.execution.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by jzb on 17-4-30.
 */
public class ManagerExamQuestionJoinCommand implements Serializable {
    @NotNull
    private EntityDTO operator;
    @NotNull
    @Size(min = 1)
    private Set<EntityDTO> tasks;

    public EntityDTO getOperator() {
        return operator;
    }

    public void setOperator(EntityDTO operator) {
        this.operator = operator;
    }

    public Set<EntityDTO> getTasks() {
        return tasks;
    }

    public void setTasks(Set<EntityDTO> tasks) {
        this.tasks = tasks;
    }
}

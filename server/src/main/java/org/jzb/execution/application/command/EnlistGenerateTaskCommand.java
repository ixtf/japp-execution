package org.jzb.execution.application.command;

/**
 * 描述：
 *
 * @author jzb 2017-11-27
 */
public class EnlistGenerateTaskCommand {
    private EntityDTO taskGroup;
    private String taskGroupName;

    public EntityDTO getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(EntityDTO taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }
}

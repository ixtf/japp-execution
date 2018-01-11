package org.jzb.execution.application.command;

import java.io.Serializable;

/**
 * Created by jzb on 17-4-15.
 */
public class PlanDownloadCommand implements Serializable {
    private EntityDTO taskGroup;
    private boolean follow;

    public EntityDTO getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(EntityDTO taskGroup) {
        this.taskGroup = taskGroup;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKCOMPLAIN")
@NamedQueries({
        @NamedQuery(name = "TaskComplain.queryByTaskId", query = "SELECT o FROM TaskComplain o WHERE o.task.id=:taskId AND o.deleted=FALSE")
})
public class TaskComplain extends AbstractLogable {
    @ManyToOne
    @NotNull
    private Task task;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String content;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

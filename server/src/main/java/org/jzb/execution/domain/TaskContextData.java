/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKCONTEXTDATA")
@NamedQueries({
        @NamedQuery(name = "TaskContextData.addReadCount", query = "UPDATE TaskContextData o SET o.readCount=o.readCount+1 WHERE o.task=:task")
})
public class TaskContextData extends AbstractEntity {
    @JsonIgnore
    @OneToOne
    @PrimaryKeyJoinColumn
    private Task task;
    private int attachmentCount;
    private int taskFeedbackCount;
    private int readCount;

    public TaskContextData(Task task) {
        this.task = task;
        this.id = task.getId();
    }

    public TaskContextData() {
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getAttachmentCount() {
        return attachmentCount;
    }

    public void setAttachmentCount(int attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    public int getTaskFeedbackCount() {
        return taskFeedbackCount;
    }

    public void setTaskFeedbackCount(int taskFeedbackCount) {
        this.taskFeedbackCount = taskFeedbackCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}

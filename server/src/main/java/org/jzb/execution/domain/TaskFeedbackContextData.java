/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKFEEDBACKCONTEXTDATA")
public class TaskFeedbackContextData extends AbstractEntity {
    @JsonIgnore
    @OneToOne
    @NotNull
    private TaskFeedback taskFeedback;
    private int commentCount;

    public TaskFeedback getTaskFeedback() {
        return taskFeedback;
    }

    public void setTaskFeedback(TaskFeedback taskFeedback) {
        this.taskFeedback = taskFeedback;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKNOTICE")
@NamedQueries({
        @NamedQuery(name = "TaskNotice.queryByTaskId", query = "SELECT o FROM TaskNotice o WHERE o.task.id=:taskId AND o.deleted=FALSE ")
})
public class TaskNotice extends AbstractEntity {
    @JsonIgnore
    @ManyToOne
    private Task task;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date noticeDateTime;
    @ManyToMany
    @JoinTable(name = "T_TASKNOTICE_T_RECEIVER")
    private Collection<Operator> receivers;
    private String content;
    private boolean noticed;

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

    public Collection<Operator> getReceivers() {
        return receivers;
    }

    public void setReceivers(Collection<Operator> receivers) {
        this.receivers = receivers;
    }

    public Date getNoticeDateTime() {
        return noticeDateTime;
    }

    public void setNoticeDateTime(Date noticeDateTime) {
        this.noticeDateTime = noticeDateTime;
    }

    public boolean isNoticed() {
        return noticed;
    }

    public void setNoticed(boolean noticed) {
        this.noticed = noticed;
    }
}

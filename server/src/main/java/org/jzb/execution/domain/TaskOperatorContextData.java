/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jzb.J;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKOPERATORCONTEXTDATA")
@NamedQueries({
        @NamedQuery(name = "TaskOperatorContextData.queryByTask", query = "SELECT o FROM TaskOperatorContextData o WHERE o.task=:task")
})
public class TaskOperatorContextData extends AbstractEntity {
    @JsonIgnore
    @ManyToOne
    private Task task;
    @ManyToOne
    private Operator operator;
    private String nickName;
    private boolean weixinShareTimeline;
    @Temporal(TemporalType.TIMESTAMP)
    private Date weixinShareTimelineDateTime;

    private int readCount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastReadDateTime;
    private boolean neverRead = true;
    private int taskFeedbackCount;
    private int taskFeedbackUnreadCount;
    private int taskFeedbackCommentCount;
    private int taskFeedbackCommentUnreadCount;

    public TaskOperatorContextData(Task task, Operator operator) {
        this.task = task;
        this.operator = operator;
        this.id = generateId(task, operator);
    }

    public TaskOperatorContextData() {
    }

    public static String generateId(Task task, Operator operator) {
        return J.uuid58(task.getId(), operator.getId());
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isWeixinShareTimeline() {
        return weixinShareTimeline;
    }

    public void setWeixinShareTimeline(boolean weixinShareTimeline) {
        this.weixinShareTimeline = weixinShareTimeline;
    }

    public Date getWeixinShareTimelineDateTime() {
        return weixinShareTimelineDateTime;
    }

    public void setWeixinShareTimelineDateTime(Date weixinShareTimelineDateTime) {
        this.weixinShareTimelineDateTime = weixinShareTimelineDateTime;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public Date getLastReadDateTime() {
        return lastReadDateTime;
    }

    public void setLastReadDateTime(Date lastReadDateTime) {
        this.lastReadDateTime = lastReadDateTime;
    }

    public int getTaskFeedbackCount() {
        return taskFeedbackCount;
    }

    public void setTaskFeedbackCount(int taskFeedbackCount) {
        this.taskFeedbackCount = taskFeedbackCount;
    }

    public int getTaskFeedbackUnreadCount() {
        return taskFeedbackUnreadCount;
    }

    public void setTaskFeedbackUnreadCount(int taskFeedbackUnreadCount) {
        this.taskFeedbackUnreadCount = taskFeedbackUnreadCount;
    }

    public int getTaskFeedbackCommentCount() {
        return taskFeedbackCommentCount;
    }

    public void setTaskFeedbackCommentCount(int taskFeedbackCommentCount) {
        this.taskFeedbackCommentCount = taskFeedbackCommentCount;
    }

    public int getTaskFeedbackCommentUnreadCount() {
        return taskFeedbackCommentUnreadCount;
    }

    public void setTaskFeedbackCommentUnreadCount(int taskFeedbackCommentUnreadCount) {
        this.taskFeedbackCommentUnreadCount = taskFeedbackCommentUnreadCount;
    }

    public boolean isNeverRead() {
        return neverRead;
    }

    public void setNeverRead(boolean neverRead) {
        this.neverRead = neverRead;
    }
}

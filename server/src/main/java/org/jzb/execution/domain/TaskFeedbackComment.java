/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jzb.execution.domain.extra.ExamQuestion;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKFEEDBACKCOMMENT")
@NamedQueries({
        @NamedQuery(name = "TaskFeedbackComment.queryByTaskFeedback", query = "SELECT o FROM TaskFeedbackComment o WHERE o.taskFeedback=:taskFeedback AND o.deleted=FALSE")
})
public class TaskFeedbackComment extends AbstractLogable implements Comparable<TaskFeedbackComment> {
    @JsonIgnore
    @ManyToOne
    @NotNull
    private TaskFeedback taskFeedback;
    private String note;
    @ManyToMany
    @JoinTable(name = "T_TASKFEEDBACKCOMMENT_T_ATTACHMENT")
    private Collection<UploadFile> attachments;
    @ManyToMany
    @JoinTable(name = "T_TASKFEEDBACKCOMMENT_T_EXAMQUESTION")
    private Collection<ExamQuestion> examQuestions;

    public TaskFeedback getTaskFeedback() {
        return taskFeedback;
    }

    public void setTaskFeedback(TaskFeedback taskFeedback) {
        this.taskFeedback = taskFeedback;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Collection<UploadFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<UploadFile> attachments) {
        this.attachments = attachments;
    }

    public Collection<ExamQuestion> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(Collection<ExamQuestion> examQuestions) {
        this.examQuestions = examQuestions;
    }

    @Override
    public int compareTo(TaskFeedbackComment o) {
        return o.getModifyDateTime().compareTo(this.getModifyDateTime());
    }

}

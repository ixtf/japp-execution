package org.jzb.execution.application.command;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-4-15.
 */

public class TaskFeedbackCommentUpdateCommand implements Serializable {
    private String note;
    private List<EntityDTO> attachments;
    private List<EntityDTO> examQuestions;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<EntityDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EntityDTO> attachments) {
        this.attachments = attachments;
    }

    public List<EntityDTO> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(List<EntityDTO> examQuestions) {
        this.examQuestions = examQuestions;
    }
}
package org.jzb.execution.application.command;

import org.jzb.execution.domain.data.FieldValue;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-4-15.
 */

public class TaskFeedbackUpdateCommand implements Serializable {
    private String note;
    private boolean atAll;
    private List<EntityDTO> atOperators;
    private List<EntityDTO> attachments;
    private List<FieldValue> fieldsValue;

    public boolean isAtAll() {
        return atAll;
    }

    public void setAtAll(boolean atAll) {
        this.atAll = atAll;
    }

    public List<EntityDTO> getAtOperators() {
        return atOperators;
    }

    public void setAtOperators(List<EntityDTO> atOperators) {
        this.atOperators = atOperators;
    }

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

    public List<FieldValue> getFieldsValue() {
        return fieldsValue;
    }

    public void setFieldsValue(List<FieldValue> fieldsValue) {
        this.fieldsValue = fieldsValue;
    }
}
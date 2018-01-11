package org.jzb.execution.application.command;

import org.jzb.execution.domain.data.FieldValue;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-4-15.
 */

public class EnlistFeedbackUpdateCommand implements Serializable {
    private String note;
    private List<EntityDTO> attachments;
    private List<FieldValue> fieldsValue;

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
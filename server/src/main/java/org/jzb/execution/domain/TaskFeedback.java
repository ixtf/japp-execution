/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang3.StringUtils;
import org.jzb.J;
import org.jzb.execution.domain.data.FieldValue;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.jzb.Constant.MAPPER;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKFEEDBACK")
@NamedQueries({
        @NamedQuery(name = "TaskFeedback.queryByTaskId", query = "SELECT o FROM TaskFeedback o WHERE o.task.id=:taskId AND o.deleted=FALSE ")
})
public class TaskFeedback extends AbstractLogable implements Comparable<TaskFeedback> {
    @JsonIgnore
    @ManyToOne
    @NotNull
    private Task task;
    private String note;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "T_TASKFEEDBACK_T_ATTACHMENT")
    private Collection<UploadFile> attachments;
    private boolean atAll;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "T_TASKFEEDBACK_T_ATOPERATOR")
    private Collection<Operator> atOperators;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fieldsValueString;

    @JsonGetter
    public Collection<FieldValue> getFieldsValue() throws IOException {
        if (StringUtils.isBlank(fieldsValueString))
            return null;
        CollectionType t = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, FieldValue.class);
        return MAPPER.readValue(fieldsValueString, t);
    }

    public void setFieldsValue(Collection<FieldValue> fieldsValue) throws JsonProcessingException {
        fieldsValueString = J.isEmpty(fieldsValue) ? null : MAPPER.writeValueAsString(fieldsValue);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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

    public String getFieldsValueString() {
        return fieldsValueString;
    }

    public void setFieldsValueString(String fieldsValueString) {
        this.fieldsValueString = fieldsValueString;
    }

    public boolean isAtAll() {
        return atAll;
    }

    public void setAtAll(boolean atAll) {
        this.atAll = atAll;
    }

    public Collection<Operator> getAtOperators() {
        return atOperators;
    }

    public void setAtOperators(Collection<Operator> atOperators) {
        this.atOperators = atOperators;
    }

    @Override
    public int compareTo(TaskFeedback o) {
        return o.getModifyDateTime().compareTo(this.getModifyDateTime());
    }

}

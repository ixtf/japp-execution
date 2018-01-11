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
import org.apache.commons.collections4.CollectionUtils;
import org.jzb.J;
import org.jzb.execution.domain.data.EvaluateFieldValue;
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
@Table(name = "T_TASKEVALUATE")
@NamedQueries({
        @NamedQuery(name = "TaskEvaluate.queryByTaskId", query = "SELECT o FROM TaskEvaluate o WHERE o.task.id=:taskId AND o.deleted=FALSE ")
})
public class TaskEvaluate extends AbstractLogable implements Comparable<TaskEvaluate> {
    @JsonIgnore
    @ManyToOne
    @NotNull
    private Task task;
    @ManyToOne
    @NotNull
    private Operator executor;
    private String note;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fieldsValueString;

    @JsonGetter
    public Collection<EvaluateFieldValue> getFieldsValue() throws IOException {
        if (J.isBlank(fieldsValueString))
            return null;
        CollectionType t = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, EvaluateFieldValue.class);
        return MAPPER.readValue(fieldsValueString, t);
    }

    public void setFieldsValue(Collection<EvaluateFieldValue> fieldsValue) throws JsonProcessingException {
        fieldsValueString = CollectionUtils.isEmpty(fieldsValue) ? null : MAPPER.writeValueAsString(fieldsValue);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Operator getExecutor() {
        return executor;
    }

    public void setExecutor(Operator executor) {
        this.executor = executor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFieldsValueString() {
        return fieldsValueString;
    }

    public void setFieldsValueString(String fieldsValueString) {
        this.fieldsValueString = fieldsValueString;
    }

    @Override
    public int compareTo(TaskEvaluate o) {
        return o.getModifyDateTime().compareTo(this.getModifyDateTime());
    }
}

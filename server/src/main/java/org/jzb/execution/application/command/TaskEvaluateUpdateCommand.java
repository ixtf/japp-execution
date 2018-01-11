package org.jzb.execution.application.command;

import org.jzb.execution.domain.data.EvaluateFieldValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-4-15.
 */
public class TaskEvaluateUpdateCommand implements Serializable {
    @NotNull
    private EntityDTO executor;
    private String note;
    @NotNull
    @Size(min = 1)
    private List<EvaluateFieldValue> fieldsValue;

    public EntityDTO getExecutor() {
        return executor;
    }

    public void setExecutor(EntityDTO executor) {
        this.executor = executor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<EvaluateFieldValue> getFieldsValue() {
        return fieldsValue;
    }

    public void setFieldsValue(List<EvaluateFieldValue> fieldsValue) {
        this.fieldsValue = fieldsValue;
    }
}
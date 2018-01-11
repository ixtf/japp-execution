package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.data.EvaluateField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-2-27.
 */
public class EvaluateTemplateUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    @Size(min = 1)
    @NotNull
    private List<EvaluateField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EvaluateField> getFields() {
        return fields;
    }

    public void setFields(List<EvaluateField> fields) {
        this.fields = fields;
    }
}

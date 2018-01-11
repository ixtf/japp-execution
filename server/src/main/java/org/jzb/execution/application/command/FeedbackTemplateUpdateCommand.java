package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.data.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-2-27.
 */
public class FeedbackTemplateUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    @Size(min = 1)
    @NotNull
    private List<Field> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}

package org.jzb.execution.domain.data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jzb on 17-2-27.
 */
public class Field implements Serializable {
    private String id;
    @NotBlank
    private String name;
    @NotNull
    private FieldType type;
    private boolean required;
    private List<String> selectOptions;

    public void uuid() {
        if (StringUtils.isBlank(id)) {
            setId(J.uuid58());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<String> getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(List<String> selectOptions) {
        this.selectOptions = selectOptions;
    }
}

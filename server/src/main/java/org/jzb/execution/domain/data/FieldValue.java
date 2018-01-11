package org.jzb.execution.domain.data;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by jzb on 17-2-27.
 */
public class FieldValue implements Serializable {
    @NotBlank
    private String id;
    private String valueString;
    //多选字段的值
    private List<String> valuesString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public List<String> getValuesString() {
        return valuesString;
    }

    public void setValuesString(List<String> valuesString) {
        this.valuesString = valuesString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldValue that = (FieldValue) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

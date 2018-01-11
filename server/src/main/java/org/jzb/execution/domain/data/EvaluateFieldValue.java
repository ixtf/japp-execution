package org.jzb.execution.domain.data;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by jzb on 17-2-27.
 */
public class EvaluateFieldValue implements Serializable {
    @NotBlank
    private String id;
    private Double value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluateFieldValue that = (EvaluateFieldValue) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by jzb on 17-4-15.
 */

public class EntityDTO implements Serializable {
    @NotBlank
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityDTO entityDTO = (EntityDTO) o;
        return Objects.equals(id, entityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

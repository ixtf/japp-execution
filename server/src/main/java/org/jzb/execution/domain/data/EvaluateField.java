package org.jzb.execution.domain.data;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;

import java.io.Serializable;

/**
 * Created by jzb on 17-2-27.
 */
public class EvaluateField implements Serializable {
    private String id;
    @NotBlank
    private String name;

    public void uuid() {
        if (J.isBlank(id)) {
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
}

package org.jzb.execution.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class OperatorsUpdateCommand implements Serializable {
    @NotNull
    @Size(min = 1)
    List<EntityDTO> operators;

    public List<EntityDTO> getOperators() {
        return operators;
    }

    public void setOperators(List<EntityDTO> operators) {
        this.operators = operators;
    }
}

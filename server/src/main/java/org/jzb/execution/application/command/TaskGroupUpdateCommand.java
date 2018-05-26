package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by jzb on 17-4-15.
 */

public class TaskGroupUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    private EntityDTO logo;
    private EntityDTO sign;
    private String signString;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityDTO getLogo() {
        return logo;
    }

    public void setLogo(EntityDTO logo) {
        this.logo = logo;
    }

    public EntityDTO getSign() {
        return sign;
    }

    public void setSign(EntityDTO sign) {
        this.sign = sign;
    }

    public String getSignString() {
        return signString;
    }

    public void setSignString(String signString) {
        this.signString = signString;
    }
}
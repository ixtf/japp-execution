package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by jzb on 17-4-15.
 */

public class ChannelUpdateCommand implements Serializable {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
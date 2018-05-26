package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author jzb 2018-04-03
 */
public class RedEnvelopeOrganizationUpdateCommand implements Serializable {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

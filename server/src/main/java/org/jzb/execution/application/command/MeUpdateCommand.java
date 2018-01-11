package org.jzb.execution.application.command;

import java.io.Serializable;

/**
 * Created by jzb on 17-4-15.
 */
public class MeUpdateCommand implements Serializable {
    private String mobile;
    private String name;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

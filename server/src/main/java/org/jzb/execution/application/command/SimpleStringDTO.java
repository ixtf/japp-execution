package org.jzb.execution.application.command;

import java.io.Serializable;

/**
 * Created by jzb on 17-4-15.
 */
public class SimpleStringDTO implements Serializable {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

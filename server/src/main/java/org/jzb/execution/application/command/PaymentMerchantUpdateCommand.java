package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

public class PaymentMerchantUpdateCommand implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    private String sub_appid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_appid() {
        return sub_appid;
    }

    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
    }
}

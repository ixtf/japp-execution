package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by jzb on 17-4-15.
 */
public class PasswordChangeCommand implements Serializable {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String loginPassword;
    @NotBlank
    private String loginPasswordAgain;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getLoginPasswordAgain() {
        return loginPasswordAgain;
    }

    public void setLoginPasswordAgain(String loginPasswordAgain) {
        this.loginPasswordAgain = loginPasswordAgain;
    }

}

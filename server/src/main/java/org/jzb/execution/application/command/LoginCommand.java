package org.jzb.execution.application.command;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by jzb on 17-4-15.
 */
public class LoginCommand implements Serializable {
    @NotBlank
    private String loginId;
    @NotBlank
    private String loginPassword;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public AuthenticationToken getShiroToken() {
        return new UsernamePasswordToken(loginId, loginPassword);
    }
}

package org.jzb.execution.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by jzb on 17-4-15.
 */
public class WeixinAuthorizeCodeToken implements AuthenticationToken {
    private final Type type;
    private final String code;
    private final String state;

    public WeixinAuthorizeCodeToken(Type type, String code, String state) {
        this.type = type;
        this.code = code;
        this.state = state;
    }

    public WeixinAuthorizeCodeToken(String code, String state) {
        this(Type.MP, code, state);
    }

    public Type getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getState() {
        return state;
    }

    @Override
    public Object getPrincipal() {
        throw new IllegalAccessError();
    }

    @Override
    public Object getCredentials() {
        throw new IllegalAccessError();
    }

    public enum Type {
        MP, OPEN
    }
}

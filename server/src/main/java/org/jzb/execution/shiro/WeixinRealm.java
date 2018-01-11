package org.jzb.execution.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.jzb.ee.JEE;
import org.jzb.execution.application.OperatorService;
import org.jzb.execution.domain.operator.Operator;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author jzb
 */
public class WeixinRealm extends WjhRealm {
    @Inject
    private OperatorService operatorService;

    @Override
    protected void onInit() {
        super.onInit();
        JEE.inject(this);
        setAuthenticationTokenClass(WeixinAuthorizeCodeToken.class);
        setCredentialsMatcher(new AllowAllCredentialsMatcher());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try {
            WeixinAuthorizeCodeToken t = (WeixinAuthorizeCodeToken) token;
            final Operator operator;
            switch (t.getType()) {
                case OPEN: {
                    operator = operatorService.openAuthorizeCode(t);
                    break;
                }
                default: {
                    operator = operatorService.mpAuthorizeCode(t);
                    break;
                }
            }
            return Optional.ofNullable(operator)
                    .filter(it -> !it.isDeleted())
                    .map(it -> new SimpleAuthenticationInfo(operator.getId(), null, getName()))
                    .orElseThrow(() -> new UnknownAccountException());
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }

}

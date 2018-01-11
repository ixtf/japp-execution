package org.jzb.execution.shiro;

import org.apache.shiro.authc.*;
import org.jzb.J;
import org.jzb.ee.JEE;
import org.jzb.execution.domain.operator.Login;
import org.jzb.execution.domain.repository.OperatorRepository;

import javax.inject.Inject;

/**
 * @author jzb
 */
public class LoginRealm extends WjhRealm {

    @Inject
    private OperatorRepository operatorRepository;

    @Override
    protected void onInit() {
        super.onInit();
        JEE.inject(this);
        setCredentialsMatcher((token, info) -> {
            Login login = operatorRepository.findLoginByLoginId((String) token.getPrincipal());
            try {
                String encryptPassword = String.valueOf((char[]) token.getCredentials());
                return !login.getOperator().isDeleted() && J.checkPassword(login.getLoginPassword(), encryptPassword);
            } catch (Exception e) {
                return false;
            }
        });
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        Login login = operatorRepository.findLoginByLoginId((String) token.getPrincipal());
        if (login == null || login.getOperator().isDeleted()) {
            throw new UnknownAccountException("No account found for user [" + token.getPrincipal() + "]");
        }
        return new SimpleAuthenticationInfo(login.getId(), token.getCredentials(), getName());
    }

}

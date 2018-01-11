package org.jzb.execution.shiro;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jzb.execution.Constant;

public abstract class WjhRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo result = new SimpleAuthorizationInfo();
        result.addRole("operator");
        boolean isAdmin = Constant.ADMIN_IDS.contains(principals.getPrimaryPrincipal());
        if (isAdmin) {
            result.addRole("admin");
        }
        return result;
    }
}

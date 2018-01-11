package org.jzb.execution.application;

import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.shiro.WeixinAuthorizeCodeToken;

/**
 * Created by jzb on 17-4-15.
 */
public interface OperatorService {

    Operator mpAuthorizeCode(WeixinAuthorizeCodeToken token) throws Exception;

    Operator openAuthorizeCode(WeixinAuthorizeCodeToken token) throws Exception;
}

package org.jzb.execution.domain.repository;


import org.jzb.execution.domain.operator.Login;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.operator.WeixinOperator;
import org.jzb.weixin.mp.unionInfo.MpUnionInfoResponse;

import java.security.Principal;

/**
 * Created by jzb on 17-2-27.
 */
public interface OperatorRepository {

    Operator save(Operator operator);

    Operator find(String id);

    Operator find(Principal principal);

    Login save(Operator operator, Login login);

    Login findLoginByLoginId(String loginId);

    Login findLogin(String id);

    WeixinOperator save(Operator operator, WeixinOperator weixinOperator, MpUnionInfoResponse mpUnionInfo);
}

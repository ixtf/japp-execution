package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.operator.WeixinOperator;
import org.jzb.weinxin.open.authorizeCode.OpenAuthorizeUnionInfoResponse;
import org.jzb.weixin.mp.authorizeCode.MpAuthorizeUnionInfoResponse;

import java.security.Principal;

/**
 * Created by jzb on 17-2-27.
 */
public interface WeixinOperatorRepository {
    WeixinOperator find(String openid);

    WeixinOperator find(MpAuthorizeUnionInfoResponse res);

    WeixinOperator find(OpenAuthorizeUnionInfoResponse res);

    WeixinOperator findByUnionid(String unionid);

    WeixinOperator find(Operator operator);

    WeixinOperator find(Principal principal);
}

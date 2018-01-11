package org.jzb.execution.application.internal;

import org.jzb.execution.application.OperatorService;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.operator.WeixinOperator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.WeixinOperatorRepository;
import org.jzb.execution.shiro.WeixinAuthorizeCodeToken;
import org.jzb.weinxin.open.OpenClient;
import org.jzb.weinxin.open.authorizeCode.OpenAuthorizeUnionInfoResponse;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.mp.authorizeCode.MpAuthorizeUnionInfoResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class OperatorServiceImpl implements OperatorService {
    @Inject
    private MpClient mpClient;
    @Inject
    private OpenClient openClient;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private WeixinOperatorRepository weixinOperatorRepository;

    @Override
    public Operator mpAuthorizeCode(WeixinAuthorizeCodeToken token) throws Exception {
        final MpAuthorizeUnionInfoResponse res = mpClient.authorizeCode(token.getCode(), token.getState()).call();
        final WeixinOperator weixinOperator = weixinOperatorRepository.find(res);
        return Optional.ofNullable(weixinOperator)
                .map(WeixinOperator::getOperator)
                .map(operator -> {
                    operator.setAvatar(res.headimgurl());
                    return operatorRepository.save(operator);
                })
                .orElse(null);
    }

    @Override
    public Operator openAuthorizeCode(WeixinAuthorizeCodeToken token) throws Exception {
        final OpenAuthorizeUnionInfoResponse res = openClient.authorizeCode(token.getCode(), token.getState()).call();
        final WeixinOperator weixinOperator = weixinOperatorRepository.find(res);
        return Optional.ofNullable(weixinOperator)
                .map(WeixinOperator::getOperator)
                .map(operator -> {
                    operator.setAvatar(res.headimgurl());
                    return operatorRepository.save(operator);
                })
                .orElse(null);
    }
}

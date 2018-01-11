package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.operator.WeixinOperator;
import org.jzb.execution.domain.repository.WeixinOperatorRepository;
import org.jzb.weinxin.open.authorizeCode.OpenAuthorizeUnionInfoResponse;
import org.jzb.weixin.mp.authorizeCode.MpAuthorizeUnionInfoResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.security.Principal;
import java.util.Optional;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaWeixinOperatorRepository extends JpaCURDRepository<WeixinOperator, String> implements WeixinOperatorRepository {
    @Override
    public WeixinOperator find(MpAuthorizeUnionInfoResponse res) {
        return Optional.ofNullable(find(res.openid()))
                .orElseGet(() -> findByUnionid(res.unionid()));
    }

    @Override
    public WeixinOperator find(OpenAuthorizeUnionInfoResponse res) {
        return Optional.ofNullable(find(res.openid()))
                .orElseGet(() -> findByUnionid(res.unionid()));
    }

    @Override
    public WeixinOperator findByUnionid(String unionid) {
        TypedQuery<WeixinOperator> typedQuery = em.createNamedQuery("WeixinOperator.findByUnionid", WeixinOperator.class)
                .setParameter("unionid", unionid);
        return querySingle(typedQuery);
    }

    @Override
    public WeixinOperator find(Operator operator) {
        return findByOperatorId(operator.getId());
    }

    @Override
    public WeixinOperator find(Principal principal) {
        return findByOperatorId(principal.getName());
    }

    private WeixinOperator findByOperatorId(String operatorId) {
        TypedQuery<WeixinOperator> typedQuery = em.createNamedQuery("WeixinOperator.findByOperatorId", WeixinOperator.class)
                .setParameter("operatorId", operatorId);
        return querySingle(typedQuery);
    }

}

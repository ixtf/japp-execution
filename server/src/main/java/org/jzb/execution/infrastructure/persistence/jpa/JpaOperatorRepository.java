package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.operator.Login;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.operator.WeixinOperator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.infrastructure.persistence.lucene.OperatorLucene;
import org.jzb.weixin.mp.unionInfo.MpUnionInfoResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.security.Principal;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaOperatorRepository extends JpaCURDRepository<Operator, String> implements OperatorRepository {
    @Inject
    private OperatorLucene operatorLucene;

    @Override
    public Operator save(Operator o) {
        Operator result = super.save(o);
        operatorLucene.index(result);
        return result;
    }

    @Override
    public Operator find(Principal principal) {
        return find(principal.getName());
    }

    @Override
    public Login save(Operator operator, Login login) {
        operator = save(operator);
        login.setOperator(operator);
        login.setId(operator.getId());
        return em.merge(login);
    }

    @Override
    public Login findLoginByLoginId(String loginId) {
        TypedQuery<Login> typedQuery = em.createNamedQuery("Login.findByLoginId", Login.class)
                .setParameter("loginId", loginId);
        return querySingle(typedQuery);
    }

    @Override
    public Login findLogin(String id) {
        return em.find(Login.class, id);
    }

    @Override
    public WeixinOperator save(Operator operator, WeixinOperator weixinOperator, MpUnionInfoResponse res) {
        operator.setAvatar(res.headimgurl());
        operator.setNickName(res.nickname());
        operator = save(operator);
        weixinOperator.setUnionInfo(res);
        weixinOperator.setOperator(operator);
        return em.merge(weixinOperator);
    }

}

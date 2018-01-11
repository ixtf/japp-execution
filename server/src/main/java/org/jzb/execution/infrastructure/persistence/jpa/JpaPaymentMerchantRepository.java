package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.PaymentMerchant;
import org.jzb.execution.domain.PaymentMerchantInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.PaymentMerchantRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaPaymentMerchantRepository extends JpaCURDRepository<PaymentMerchant, String> implements PaymentMerchantRepository {
    @Override
    public Stream<PaymentMerchant> queryByManager(Operator manager) {
        return em.createNamedQuery("PaymentMerchant.queryByManager", PaymentMerchant.class)
                .setParameter("manager", manager)
                .getResultList()
                .stream();
    }

    @Override
    public PaymentMerchantInvite save(PaymentMerchantInvite invite) {
        return em.merge(invite);
    }

    @Override
    public PaymentMerchantInvite findPaymentMerchantInviteByTicket(String ticket) {
        return em.find(PaymentMerchantInvite.class, ticket);
    }
}

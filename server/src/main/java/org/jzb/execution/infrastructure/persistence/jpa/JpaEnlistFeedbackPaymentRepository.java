package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.ee.JEE;
import org.jzb.execution.domain.EnlistFeedbackPayment;
import org.jzb.execution.domain.repository.EnlistFeedbackPaymentRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaEnlistFeedbackPaymentRepository extends JpaCURDRepository<EnlistFeedbackPayment, String> implements EnlistFeedbackPaymentRepository {
    @Override
    public EnlistFeedbackPayment findByOutTradeNo(String out_trade_no) {
        final TypedQuery<EnlistFeedbackPayment> query = em.createNamedQuery("EnlistFeedbackPayment.findByOutTradeNo", EnlistFeedbackPayment.class)
                .setParameter("out_trade_no", out_trade_no);
        return JEE.getSingle(query);
    }
}

package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.EnlistFeedbackPayment;

/**
 * Created by jzb on 17-2-27.
 */
public interface EnlistFeedbackPaymentRepository {
    EnlistFeedbackPayment save(EnlistFeedbackPayment enlistFeedbackPayment);

    EnlistFeedbackPayment find(String id);

    void delete(String id);

    EnlistFeedbackPayment findByOutTradeNo(String out_trade_no);
}

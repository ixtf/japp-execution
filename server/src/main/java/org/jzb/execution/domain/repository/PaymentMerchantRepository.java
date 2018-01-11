package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.PaymentMerchant;
import org.jzb.execution.domain.PaymentMerchantInvite;
import org.jzb.execution.domain.operator.Operator;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface PaymentMerchantRepository {
    PaymentMerchant save(PaymentMerchant paymentMerchant);

    PaymentMerchant find(String id);

    void delete(String id);

    Stream<PaymentMerchant> findAll();

    Stream<PaymentMerchant> queryByManager(Operator manager);

    PaymentMerchantInvite save(PaymentMerchantInvite invite);

    PaymentMerchantInvite findPaymentMerchantInviteByTicket(String ticket);
}

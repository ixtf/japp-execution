package org.jzb.execution.application;

import org.jzb.execution.application.command.PaymentMerchantUpdateCommand;
import org.jzb.execution.domain.PaymentMerchant;
import org.jzb.execution.domain.PaymentMerchantInvite;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface PaymentMerchantService {
    PaymentMerchant create(Principal principal, PaymentMerchantUpdateCommand command);

    PaymentMerchant update(Principal principal, String id, PaymentMerchantUpdateCommand command);

    void delete(Principal principal, String id);

    void addManager(Principal principal, String id, String managerId);

    void removeManager(Principal principal, String id, String managerId);

    PaymentMerchantInvite inviteTicket(Principal principal, String id) throws Exception;

    void deleteManager(Principal principal, String id, String managerId) throws Exception;
}

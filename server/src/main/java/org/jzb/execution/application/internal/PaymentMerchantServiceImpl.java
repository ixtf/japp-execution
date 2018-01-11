package org.jzb.execution.application.internal;

import com.google.common.collect.Sets;
import org.jzb.J;
import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.application.PaymentMerchantService;
import org.jzb.execution.application.command.PaymentMerchantUpdateCommand;
import org.jzb.execution.domain.PaymentMerchant;
import org.jzb.execution.domain.PaymentMerchantInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.PaymentMerchantRepository;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.mp.qrcode.MpQrcodeResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jzb.execution.domain.data.WX_QR_SCENE_ID.PAYMENTMERCHANT_INVITE;

@Stateless
public class PaymentMerchantServiceImpl implements PaymentMerchantService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private PaymentMerchantRepository paymentMerchantRepository;
    @Inject
    private MpClient mpClient;

    @Override
    public PaymentMerchant create(Principal principal, PaymentMerchantUpdateCommand command) {
        return save(principal, new PaymentMerchant(), command);
    }

    private PaymentMerchant save(Principal principal, PaymentMerchant o, PaymentMerchantUpdateCommand command) {
        o.setName(command.getName());
        o.setSub_appid(command.getSub_appid());
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return paymentMerchantRepository.save(o);
    }

    @Override
    public PaymentMerchant update(Principal principal, String id, PaymentMerchantUpdateCommand command) {
        return save(principal, paymentMerchantRepository.find(id), command);
    }

    @Override
    public void delete(Principal principal, String id) {
        paymentMerchantRepository.delete(id);
    }

    @Override
    public void addManager(Principal principal, String id, String managerId) {
        final PaymentMerchant paymentMerchant = paymentMerchantRepository.find(id);
        Set<Operator> managers = Sets.newHashSet(operatorRepository.find(managerId));
        managers.addAll(J.emptyIfNull(paymentMerchant.getManagers()));
        paymentMerchant.setManagers(managers);
        paymentMerchantRepository.save(paymentMerchant);
    }

    @Override
    public void removeManager(Principal principal, String id, String managerId) {
        final PaymentMerchant paymentMerchant = paymentMerchantRepository.find(id);
        Set<Operator> managers = J.emptyIfNull(paymentMerchant.getManagers())
                .parallelStream()
                .filter(it -> !it.getId().equals(managerId))
                .collect(Collectors.toSet());
        paymentMerchant.setManagers(managers);
        paymentMerchantRepository.save(paymentMerchant);
    }

    @Override
    public PaymentMerchantInvite inviteTicket(Principal principal, String id) throws Exception {
        final PaymentMerchant paymentMerchant = paymentMerchantRepository.find(id);
        final MpQrcodeResponse res = mpClient.qrcode()
                .expire_seconds(2592000)
                .action_name("QR_SCENE")
                .scene_id(PAYMENTMERCHANT_INVITE.scene_id())
                .call();
        PaymentMerchantInvite invite = new PaymentMerchantInvite();
        invite.setPaymentMerchant(paymentMerchant);
        invite.setTicket(res.ticket());
        invite.setExpireSeconds(res.expire_seconds());
        return paymentMerchantRepository.save(invite);
    }

    @Override
    public void deleteManager(Principal principal, String id, String managerId) throws Exception {
        final PaymentMerchant paymentMerchant = paymentMerchantRepository.find(id);
        if (!paymentMerchant.isManager(principal)) {
            throw new JNonAuthorizationError();
        }
        final Set<Operator> managers = J.emptyIfNull(paymentMerchant.getManagers())
                .stream()
                .filter(it -> !it.getId().equals(managerId))
                .collect(Collectors.toSet());
        paymentMerchant.setManagers(managers);
        paymentMerchantRepository.save(paymentMerchant);
    }
}

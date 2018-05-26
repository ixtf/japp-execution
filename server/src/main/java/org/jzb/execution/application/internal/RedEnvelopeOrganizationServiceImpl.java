package org.jzb.execution.application.internal;

import com.google.common.collect.Sets;
import org.jzb.J;
import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.application.RedEnvelopeOrganizationService;
import org.jzb.execution.application.command.RedEnvelopeOrganizationUpdateCommand;
import org.jzb.execution.domain.RedEnvelopeOrganization;
import org.jzb.execution.domain.RedEnvelopeOrganizationInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.RedEnvelopeOrganizationRepository;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.mp.qrcode.MpQrcodeResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jzb.execution.domain.data.WX_QR_SCENE_ID.REDENVELOPEORGANIZATION_INVITE;

@Stateless
public class RedEnvelopeOrganizationServiceImpl implements RedEnvelopeOrganizationService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private RedEnvelopeOrganizationRepository redEnvelopeOrganizationRepository;
    @Inject
    private MpClient mpClient;

    @Override
    public RedEnvelopeOrganization create(Principal principal, RedEnvelopeOrganizationUpdateCommand command) {
        return save(principal, new RedEnvelopeOrganization(), command);
    }

    private RedEnvelopeOrganization save(Principal principal, RedEnvelopeOrganization o, RedEnvelopeOrganizationUpdateCommand command) {
        o.setName(command.getName());
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return redEnvelopeOrganizationRepository.save(o);
    }

    @Override
    public RedEnvelopeOrganization update(Principal principal, String id, RedEnvelopeOrganizationUpdateCommand command) {
        return save(principal, redEnvelopeOrganizationRepository.find(id), command);
    }

    @Override
    public void delete(Principal principal, String id) {
        redEnvelopeOrganizationRepository.delete(id);
    }

    @Override
    public void addManager(Principal principal, String id, String managerId) {
        final RedEnvelopeOrganization redEnvelopeOrganization = redEnvelopeOrganizationRepository.find(id);
        Set<Operator> managers = Sets.newHashSet(operatorRepository.find(managerId));
        managers.addAll(J.emptyIfNull(redEnvelopeOrganization.getManagers()));
        redEnvelopeOrganization.setManagers(managers);
        redEnvelopeOrganizationRepository.save(redEnvelopeOrganization);
    }

    @Override
    public void removeManager(Principal principal, String id, String managerId) {
        final RedEnvelopeOrganization redEnvelopeOrganization = redEnvelopeOrganizationRepository.find(id);
        if (!redEnvelopeOrganization.isManager(principal)) {
            throw new JNonAuthorizationError();
        }
        final Set<Operator> managers = J.emptyIfNull(redEnvelopeOrganization.getManagers())
                .stream()
                .filter(it -> !it.getId().equals(managerId))
                .collect(Collectors.toSet());
        redEnvelopeOrganization.setManagers(managers);
        redEnvelopeOrganizationRepository.save(redEnvelopeOrganization);
    }

    @Override
    public RedEnvelopeOrganizationInvite inviteTicket(Principal principal, String id) throws Exception {
        final RedEnvelopeOrganization redEnvelopeOrganization = redEnvelopeOrganizationRepository.find(id);
        final MpQrcodeResponse res = mpClient.qrcode()
                .expire_seconds(2592000)
                .action_name("QR_SCENE")
                .scene_id(REDENVELOPEORGANIZATION_INVITE.scene_id())
                .call();
        RedEnvelopeOrganizationInvite invite = new RedEnvelopeOrganizationInvite();
        invite.setRedEnvelopeOrganization(redEnvelopeOrganization);
        invite.setTicket(res.ticket());
        invite.setExpireSeconds(res.expire_seconds());
        return redEnvelopeOrganizationRepository.save(invite);
    }
}

package org.jzb.execution.application;

import org.jzb.execution.application.command.RedEnvelopeOrganizationUpdateCommand;
import org.jzb.execution.domain.RedEnvelopeOrganization;
import org.jzb.execution.domain.RedEnvelopeOrganizationInvite;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface RedEnvelopeOrganizationService {

    RedEnvelopeOrganization create(Principal principal, RedEnvelopeOrganizationUpdateCommand command);

    RedEnvelopeOrganization update(Principal principal, String id, RedEnvelopeOrganizationUpdateCommand command);

    void delete(Principal principal, String id);

    void addManager(Principal principal, String id, String managerId);

    void removeManager(Principal principal, String id, String managerId);

    RedEnvelopeOrganizationInvite inviteTicket(Principal principal, String id) throws Exception;

}

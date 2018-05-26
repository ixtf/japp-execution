package org.jzb.execution.application;

import org.jzb.execution.application.command.ChannelUpdateCommand;
import org.jzb.execution.application.command.PlanAuditCommand;
import org.jzb.execution.domain.Channel;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface AdminService {
    void auditPlan(Principal principal, String planId, PlanAuditCommand command);

    void unAuditPlan(Principal principal, String planId);

    Channel create(Principal principal, ChannelUpdateCommand command);

    Channel update(Principal principal, String id, ChannelUpdateCommand command);

    void deleteChannel(Principal principal, String id);

    void deleteRedEnvelopeOrganization(Principal principal, String id);
}

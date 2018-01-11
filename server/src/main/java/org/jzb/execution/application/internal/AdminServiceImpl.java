package org.jzb.execution.application.internal;

import org.jzb.execution.application.AdminService;
import org.jzb.execution.application.command.ChannelUpdateCommand;
import org.jzb.execution.application.command.PlanAuditCommand;
import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.repository.ChannelRepository;
import org.jzb.execution.domain.repository.PlanRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class AdminServiceImpl implements AdminService {
    @Inject
    private ChannelRepository channelRepository;
    @Inject
    private PlanRepository planRepository;

    @Override
    public void auditPlan(Principal principal, String planId, PlanAuditCommand command) {
        Plan plan = planRepository.find(planId);
        plan.setChannel(channelRepository.find(command.getChannel().getId()));
        plan.setAudited(true);
        planRepository.save(plan);
    }

    @Override
    public void unAuditPlan(Principal principal, String planId) {
        Plan plan = planRepository.find(planId);
        plan.setAudited(false);
        plan.setPublished(false);
        planRepository.save(plan);
    }

    @Override
    public Channel create(Principal principal, ChannelUpdateCommand command) {
        return save(principal, new Channel(), command);
    }

    private Channel save(Principal principal, Channel o, ChannelUpdateCommand command) {
        o.setName(command.getName());
        return channelRepository.save(o);
    }

    @Override
    public Channel update(Principal principal, String id, ChannelUpdateCommand command) {
        Channel channel = channelRepository.find(id);
        return save(principal, channel, command);
    }

    @Override
    public void deleteChannel(Principal principal, String id) {
        channelRepository.delete(id);
    }
}

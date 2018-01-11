package org.jzb.execution.application;

import org.jzb.execution.application.command.PlanDownloadCommand;
import org.jzb.execution.application.command.PlanUpdateCommand;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.PlanFollow;
import org.jzb.execution.domain.PlanInvite;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface PlanService {
    Plan create(Principal principal, PlanUpdateCommand command);

    Plan update(Principal principal, String id, PlanUpdateCommand command) throws Exception;

    void delete(Principal principal, String id);

    PlanInvite inviteTicket(Principal principal, String id) throws Exception;

    void publish(Principal principal, String id) throws Exception;

    void unPublish(Principal principal, String id) throws Exception;

    PlanFollow follow(Principal principal, String id) throws Exception;

    void unFollow(Principal principal, String id) throws Exception;

    void download(Principal principal, String id, PlanDownloadCommand command) throws Exception;
}

package org.jzb.execution.application;

import org.jzb.execution.application.command.EnlistGenerateTaskCommand;
import org.jzb.execution.application.command.EnlistUpdateCommand;
import org.jzb.execution.domain.Enlist;
import org.jzb.execution.domain.EnlistInvite;
import org.jzb.execution.domain.Task;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface EnlistService {

    Enlist create(Principal principal, String paymentMerchantId, EnlistUpdateCommand command) throws Exception;

    Enlist update(Principal principal, String id, EnlistUpdateCommand command) throws Exception;

    void delete(Principal principal, String id) throws Exception;

    EnlistInvite inviteTicket(Principal principal, String id) throws Exception;

    void finish(Principal principal, String id);

    void restart(Principal principal, String id);

    Task generateTask(Principal principal, String id, EnlistGenerateTaskCommand command);
}

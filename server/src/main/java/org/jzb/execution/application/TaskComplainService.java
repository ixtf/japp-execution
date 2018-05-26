package org.jzb.execution.application;

import org.jzb.execution.application.command.TaskComplainUpdateCommand;
import org.jzb.execution.domain.TaskComplain;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface TaskComplainService {
    TaskComplain create(Principal principal, String taskId, TaskComplainUpdateCommand command) throws Exception;

    TaskComplain update(Principal principal, String id, TaskComplainUpdateCommand command) throws Exception;

    void delete(Principal principal, String id);
}

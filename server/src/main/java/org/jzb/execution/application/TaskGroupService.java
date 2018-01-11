package org.jzb.execution.application;

import org.jzb.execution.application.command.TaskGroupUpdateCommand;
import org.jzb.execution.domain.TaskGroup;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface TaskGroupService {
    TaskGroup create(Principal principal, TaskGroupUpdateCommand command);

    TaskGroup update(Principal principal, String id, TaskGroupUpdateCommand command);

    void top(Principal principal, String id);
}

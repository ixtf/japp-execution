package org.jzb.execution.application.internal;

import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.application.ApplicationEvents;
import org.jzb.execution.application.TaskComplainService;
import org.jzb.execution.application.command.TaskComplainUpdateCommand;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskComplain;
import org.jzb.execution.domain.event.EventType;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.TaskComplainRepository;
import org.jzb.execution.domain.repository.TaskRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class TaskComplainServiceImpl implements TaskComplainService {
    @Inject
    private TaskComplainRepository taskComplainRepository;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private ApplicationEvents applicationEvents;

    private void checkUpdateAuth(Principal principal, TaskComplain taskComplain) {
        final Operator operator = operatorRepository.find(principal);
        if (operator.isAdmin()) {
            return;
        }
        if (taskComplain.getModifier().equals(operator)) {
            return;
        }
        throw new JNonAuthorizationError();
    }

    @Override
    public TaskComplain create(Principal principal, String taskId, TaskComplainUpdateCommand command) throws Exception {
        final Task task = taskRepository.find(taskId);
        TaskComplain taskComplain = new TaskComplain();
        taskComplain.setTask(task);
        taskComplain = save(principal, taskComplain, command);
        applicationEvents.fireCurd(principal, TaskComplain.class, taskComplain.getId(), EventType.CREATE, command);
        return taskComplain;
    }

    private TaskComplain save(Principal principal, TaskComplain o, TaskComplainUpdateCommand command) {
        o.setContent(command.getContent());
        Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return taskComplainRepository.save(o);
    }

    @Override
    public TaskComplain update(Principal principal, String id, TaskComplainUpdateCommand command) throws Exception {
        TaskComplain taskComplain = taskComplainRepository.find(id);
        checkUpdateAuth(principal, taskComplain);
        taskComplain = save(principal, taskComplain, command);
        applicationEvents.fireCurd(principal, TaskComplain.class, taskComplain.getId(), EventType.UPDATE, command);
        return taskComplain;
    }

    @Override
    public void delete(Principal principal, String id) {
        final TaskComplain taskComplain = taskComplainRepository.find(id);
        checkUpdateAuth(principal, taskComplain);
        taskComplainRepository.delete(id);
    }
}

package org.jzb.execution.application.internal;

import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.application.TaskGroupService;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.application.command.TaskGroupUpdateCommand;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.UploadFile;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.TaskGroupRepository;
import org.jzb.execution.domain.repository.TaskRepository;
import org.jzb.execution.domain.repository.UploadFileRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Optional;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class TaskGroupServiceImpl implements TaskGroupService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private UploadFileRepository uploadFileRepository;
    @Inject
    private TaskGroupRepository taskGroupRepository;
    @Inject
    private TaskRepository taskRepository;

    private void checkUpdateAuth(Principal principal, TaskGroup taskGroup) {
        if (!taskGroup.getModifier().getId().equals(principal.getName())) {
            throw new JNonAuthorizationError();
        }
    }

    @Override
    public TaskGroup create(Principal principal, TaskGroupUpdateCommand command) {
        TaskGroup taskGroup = save(principal, new TaskGroup(), command);
        Operator operator = operatorRepository.find(principal);
        Task task = new Task();
        task.setTaskGroup(taskGroup);
        task.setTitle(command.getName());
        task.setCharger(operator);
        task._loginfo(operator);
        taskRepository.save(task);
        return taskGroup;
    }

    private TaskGroup save(Principal principal, TaskGroup o, TaskGroupUpdateCommand command) {
        o.setName(command.getName());
        UploadFile logo = Optional.ofNullable(command.getLogo())
                .map(EntityDTO::getId)
                .map(uploadFileRepository::find)
                .orElse(null);
        o.setLogo(logo);
        UploadFile sign = Optional.ofNullable(command.getSign())
                .map(EntityDTO::getId)
                .map(uploadFileRepository::find)
                .orElse(null);
        o.setSign(sign);
        o.setSignString(command.getSignString());
        Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return taskGroupRepository.save(o);
    }

    @Override
    public TaskGroup update(Principal principal, String id, TaskGroupUpdateCommand command) {
        if ("0".equals(id)) {
            throw new RuntimeException();
        }
        final TaskGroup taskGroup = taskGroupRepository.find(id);
        checkUpdateAuth(principal, taskGroup);
        return save(principal, taskGroup, command);
    }

    @Override
    public void top(Principal principal, String id) {
        if ("0".equals(id)) {
            return;
        }
        final TaskGroup taskGroup = taskGroupRepository.find(id);
        checkUpdateAuth(principal, taskGroup);
        final Operator operator = operatorRepository.find(principal);
        taskGroup._loginfo(operator);
        taskGroupRepository.save(taskGroup);
    }
}

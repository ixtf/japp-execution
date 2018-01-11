package org.jzb.execution.infrastructure.messaging.jms;

import com.google.common.collect.Sets;
import org.jzb.J;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskFeedback;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.TaskOperatorContextData;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.TaskFeedbackCommentRepository;
import org.jzb.execution.domain.repository.TaskFeedbackRepository;
import org.jzb.execution.domain.repository.TaskOperatorContextDataRepository;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by jzb on 17-4-15.
 */
@Singleton
@AccessTimeout(value = 1, unit = TimeUnit.HOURS)
public class ContextDataService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;
    @Inject
    private TaskOperatorContextDataRepository taskOperatorContextDataRepository;

    public void taskCreated(Principal principal, Task task) {
        Operator operator = operatorRepository.find(principal);
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
    }

    public void taskUpdated(Principal principal, Task task) {
        Operator operator = operatorRepository.find(principal);
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
        taskOperatorContextData.setNeverRead(true);
        taskOperatorContextDataRepository.save(taskOperatorContextData);
    }

    public void taskReaded(Principal principal, Task task) {
        Operator operator = operatorRepository.find(principal);
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
        if (task.isStarted()) {
            taskOperatorContextData.setNeverRead(false);
        }
        int readCount = taskOperatorContextData.getReadCount();
        taskOperatorContextData.setReadCount(++readCount);
        taskOperatorContextData.setLastReadDateTime(new Date());
        taskOperatorContextData.setTaskFeedbackUnreadCount(0);
        taskOperatorContextData.setTaskFeedbackCommentUnreadCount(0);
        taskOperatorContextDataRepository.save(taskOperatorContextData);
    }

    public void taskFeedbackCreated(Principal principal, String taskFeedbackId) {
        final Operator operator = operatorRepository.find(principal);
        final TaskFeedback taskFeedback = taskFeedbackRepository.find(taskFeedbackId);
        final Task task = taskFeedback.getTask();
        final TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
        int taskFeedbackCount = taskOperatorContextData.getTaskFeedbackCount();
        taskOperatorContextData.setTaskFeedbackCount(++taskFeedbackCount);
        taskOperatorContextDataRepository.save(taskOperatorContextData);

        Collection<Operator> followers = Sets.newHashSet(task.getCharger());
        followers.addAll(J.emptyIfNull(task.getFollowers()));
        if (taskFeedback.isAtAll()) {
            followers.addAll(J.emptyIfNull(task.getParticipants()));
        } else {
            followers.addAll(J.emptyIfNull(taskFeedback.getAtOperators()));
        }
        followers.stream()
                .forEach(follower -> {
                    TaskOperatorContextData contextData = taskOperatorContextDataRepository.find(task, follower);
                    int taskFeedbackUnreadCount = contextData.getTaskFeedbackUnreadCount();
                    contextData.setTaskFeedbackUnreadCount(++taskFeedbackUnreadCount);
                    taskOperatorContextDataRepository.save(contextData);
                });
    }

    public void taskFeedbackUpdated(Principal principal, String taskFeedbackId) {
        Operator operator = operatorRepository.find(principal);
        TaskFeedback taskFeedback = taskFeedbackRepository.find(taskFeedbackId);
        Task task = taskFeedback.getTask();
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
    }

    public void taskFeedbackDeleted(Principal principal, String taskFeedbackId) {
        Operator operator = operatorRepository.find(principal);
        TaskFeedback taskFeedback = taskFeedbackRepository.find(taskFeedbackId);
        Task task = taskFeedback.getTask();
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
        int taskFeedbackCount = taskOperatorContextData.getTaskFeedbackCount();
        taskOperatorContextData.setTaskFeedbackCount(--taskFeedbackCount);
        taskOperatorContextDataRepository.save(taskOperatorContextData);

        Collection<Operator> followers = Sets.newHashSet(task.getCharger());
        followers.addAll(J.emptyIfNull(task.getFollowers()));
        followers.stream()
                .forEach(follower -> {
                    TaskOperatorContextData contextData = taskOperatorContextDataRepository.find(task, follower);
                    int taskFeedbackUnreadCount = contextData.getTaskFeedbackUnreadCount();
                    contextData.setTaskFeedbackUnreadCount(--taskFeedbackUnreadCount);
                    taskOperatorContextDataRepository.save(contextData);
                });
    }

    public void taskFeedbackCommentCreated(Principal principal, String taskFeedbackCommentId) {
        Operator operator = operatorRepository.find(principal);
        TaskFeedbackComment taskFeedbackComment = taskFeedbackCommentRepository.find(taskFeedbackCommentId);
        TaskFeedback taskFeedback = taskFeedbackComment.getTaskFeedback();
        Task task = taskFeedback.getTask();
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
        int taskFeedbackCommentCount = taskOperatorContextData.getTaskFeedbackCommentCount();
        taskOperatorContextData.setTaskFeedbackCommentCount(++taskFeedbackCommentCount);
        taskOperatorContextDataRepository.save(taskOperatorContextData);

        TaskOperatorContextData contextData = taskOperatorContextDataRepository.find(task, taskFeedback.getCreator());
        int taskFeedbackCommentUnreadCount = contextData.getTaskFeedbackCommentUnreadCount();
        contextData.setTaskFeedbackCommentUnreadCount(++taskFeedbackCommentUnreadCount);
        taskOperatorContextDataRepository.save(contextData);

    }

    public void taskFeedbackCommentUpdated(Principal principal, String taskFeedbackCommentId) {
        Operator operator = operatorRepository.find(principal);
        TaskFeedbackComment taskFeedbackComment = taskFeedbackCommentRepository.find(taskFeedbackCommentId);
        TaskFeedback taskFeedback = taskFeedbackComment.getTaskFeedback();
        Task task = taskFeedback.getTask();
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
    }

    public void taskFeedbackCommentDeleted(Principal principal, String taskFeedbackCommentId) {
        Operator operator = operatorRepository.find(principal);
        TaskFeedbackComment taskFeedbackComment = taskFeedbackCommentRepository.find(taskFeedbackCommentId);
        TaskFeedback taskFeedback = taskFeedbackComment.getTaskFeedback();
        Task task = taskFeedback.getTask();
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
        int taskFeedbackCommentCount = taskOperatorContextData.getTaskFeedbackCommentCount();
        taskOperatorContextData.setTaskFeedbackCommentCount(--taskFeedbackCommentCount);
        taskOperatorContextDataRepository.save(taskOperatorContextData);

        TaskOperatorContextData contextData = taskOperatorContextDataRepository.find(task, taskFeedback.getCreator());
        int taskFeedbackCommentUnreadCount = contextData.getTaskFeedbackCommentUnreadCount();
        contextData.setTaskFeedbackCommentUnreadCount(--taskFeedbackCommentUnreadCount);
        taskOperatorContextDataRepository.save(contextData);
    }
}

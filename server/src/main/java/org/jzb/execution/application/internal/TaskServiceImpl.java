package org.jzb.execution.application.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jzb.J;
import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.Constant;
import org.jzb.execution.application.ApplicationEvents;
import org.jzb.execution.application.TaskService;
import org.jzb.execution.application.command.*;
import org.jzb.execution.domain.*;
import org.jzb.execution.domain.data.TaskStatus;
import org.jzb.execution.domain.event.EventType;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.*;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.mp.qrcode.MpQrcodeResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jzb.execution.domain.data.WX_QR_SCENE_ID.TASKS_FOLLOW_INVITE;
import static org.jzb.execution.domain.data.WX_QR_SCENE_ID.TASKS_INVITE;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class TaskServiceImpl implements TaskService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskOperatorContextDataRepository taskOperatorContextDataRepository;
    @Inject
    private UploadFileRepository uploadFileRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskEvaluateRepository taskEvaluateRepository;
    @Inject
    private TaskGroupRepository taskGroupRepository;
    @Inject
    private EvaluateTemplateRepository evaluateTemplateRepository;
    @Inject
    private TaskNoticeRepository taskNoticeRepository;
    @Inject
    private FeedbackTemplateRepository feedbackTemplateRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;
    @Inject
    private ExamQuestionRepository examQuestionRepository;
    @Inject
    private ApplicationEvents applicationEvents;
    @Inject
    private MpClient mpClient;

    private void checkUpdateAuth(Principal principal, Task task) {
        if (!task.isManager(principal) || task.isFinish() || task.isDeleted()) {
            throw new JNonAuthorizationError();
        }
    }

    @Override
    public Task create(Principal principal, TaskUpdateCommand command) throws Exception {
        Task task = save(principal, new Task(), command);
        applicationEvents.fireCurd(principal, Task.class, task.getId(), EventType.CREATE, command);
        return task;
    }

    private Task save(Principal principal, Task o, TaskUpdateCommand command) {
        final Operator operator = operatorRepository.find(principal);
        if (o.getCharger() == null) {
            o.setCharger(operator);
        }
        o.setTitle(command.getTitle());
        o.setContent(command.getContent());
        o.setStartDate(command.getStartDate());
        o.setEndDate(command.getStartDate());
        TaskGroup taskGroup = Optional.ofNullable(command.getTaskGroup())
                .map(EntityDTO::getId)
                .map(taskGroupRepository::find)
                .orElse(null);
        o.setTaskGroup(taskGroup);
        Collection<UploadFile> attachments = J.emptyIfNull(command.getAttachments())
                .stream()
                .map(EntityDTO::getId)
                .map(uploadFileRepository::find)
                .collect(Collectors.toSet());
        o.setAttachments(attachments);
        FeedbackTemplate feedbackTemplate = Optional.ofNullable(command.getFeedbackTemplate())
                .map(EntityDTO::getId)
                .map(feedbackTemplateRepository::find)
                .orElse(null);
        o.setFeedbackTemplate(feedbackTemplate);
        EvaluateTemplate evaluateTemplate = Optional.ofNullable(command.getEvaluateTemplate())
                .map(EntityDTO::getId)
                .map(evaluateTemplateRepository::find)
                .orElse(null);
        o.setEvaluateTemplate(evaluateTemplate);
        o._loginfo(operator);
        return taskRepository.save(o);
    }

    @Override
    public Task update(Principal principal, String id, TaskUpdateCommand command) throws Exception {
        Task task = taskRepository.find(id);
        checkUpdateAuth(principal, task);
        task = save(principal, task, command);
        applicationEvents.fireCurd(principal, Task.class, task.getId(), EventType.UPDATE, command);
        return task;
    }

    @Override
    public void updateNickName(Principal principal, String id, String nickName) {
        Task task = taskRepository.find(id);
        Stream<Operator> stream1 = Stream.of(task.getCharger());
        Stream<Operator> stream2 = task.getParticipants().stream();
        Stream.concat(stream1, stream2)
                .filter(operator -> operator.getId().equals(principal.getName()))
                .findFirst()
                .ifPresent(operator -> {
                    TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
                    taskOperatorContextData.setNickName(nickName);
                    taskOperatorContextDataRepository.save(taskOperatorContextData);
                });
    }

    @Override
    public TasksInvite inviteTicket(Principal principal, TaskBatchBaseCommand command) throws Exception {
        Collection<Task> tasks = command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .distinct()
                .map(taskRepository::find)
                .filter(task -> task.isManager(principal))
                .collect(Collectors.toSet());
        if (J.isEmpty(tasks)) {
            throw new JNonAuthorizationError();
        }
        final MpQrcodeResponse res = mpClient.qrcode()
                .expire_seconds(2592000)
                .action_name("QR_SCENE")
                .scene_id(TASKS_INVITE.scene_id())
                .build()
                .call();
        TasksInvite invite = new TasksInvite();
        invite.setTasks(tasks);
        invite.setTicket(res.ticket());
        invite.setExpireSeconds(res.expire_seconds());
        return taskRepository.save(invite);
    }

    @Override
    public TasksFollowInvite followInviteTicket(Principal principal, TasksFollowInviteCommand command) throws Exception {
        Collection<Task> tasks = command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .distinct()
                .map(taskRepository::find)
                .filter(task -> task.isManager(principal))
                .collect(Collectors.toSet());
        if (J.isEmpty(tasks)) {
            throw new JNonAuthorizationError();
        }
        final MpQrcodeResponse res = mpClient.qrcode()
                .expire_seconds(2592000)
                .action_name("QR_SCENE")
                .scene_id(TASKS_FOLLOW_INVITE.scene_id())
                .build()
                .call();
        TasksFollowInvite invite = new TasksFollowInvite();
        invite.setTasks(tasks);
        invite.setTicket(res.ticket());
        invite.setExpireSeconds(res.expire_seconds());
        return taskRepository.save(invite);
    }

    @Override
    public void delete(Principal principal, String id) {
        Task task = taskRepository.find(id);
        if (task.getCharger().getId().equals(principal.getName())) {
            taskRepository.delete(id);
        }
    }

    @Override
    public Task copy(Principal principal, String id) {
        Task oldTask = taskRepository.find(id);
        checkUpdateAuth(principal, oldTask);
        Task task = new Task();
        task.setTitle(oldTask.getTitle() + "[复制]");
        task.setCharger(oldTask.getCharger());
        task.setParticipants(oldTask.getParticipants());
        task.setFollowers(oldTask.getFollowers());
        task.setTaskGroup(oldTask.getTaskGroup());
        task.setContent(oldTask.getContent());
        task.setAttachments(oldTask.getAttachments());
        task.setFeedbackTemplateString(oldTask.getFeedbackTemplateString());
        task.setEvaluateTemplateString(oldTask.getEvaluateTemplateString());
        final Operator operator = operatorRepository.find(principal);
        task._loginfo(operator);
        task = taskRepository.save(task);
        copyTaskOperatorContextData(task, oldTask);
        return task;
    }

    private void copyTaskOperatorContextData(Task task, Task oldTask) {
        taskOperatorContextDataRepository.queryBy(oldTask)
                .forEach(oldTaskOperatorContextData -> {
                    Operator operator = oldTaskOperatorContextData.getOperator();
                    TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
                    taskOperatorContextData.setNickName(oldTaskOperatorContextData.getNickName());
                    taskOperatorContextDataRepository.save(taskOperatorContextData);
                });
    }

    @Override
    public void weixinShareTimeline(Principal principal, String id) throws Exception {
        Operator operator = operatorRepository.find(principal);
        Task task = taskRepository.find(id);
        TaskOperatorContextData taskOperatorContextData = taskOperatorContextDataRepository.find(task, operator);
        taskOperatorContextData.setWeixinShareTimeline(true);
        taskOperatorContextData.setWeixinShareTimelineDateTime(new Date());
        taskOperatorContextDataRepository.save(taskOperatorContextData);
    }

    @Override
    public void finish(Principal principal, String id) {
        Task task = taskRepository.find(id);
        checkUpdateAuth(principal, task);
        if (task.isDeleted() || task.getStatus() == TaskStatus.FINISH) {
            return;
        }
        task.setStatus(TaskStatus.FINISH);
        taskRepository.save(task);
    }

    @Override
    public void restart(Principal principal, String id) {
        Task task = taskRepository.find(id);
        // checkUpdateAuth(principal, task);
        if (task.isDeleted() || task.getStatus() == TaskStatus.RUN) {
            return;
        }
        task.setStatus(TaskStatus.RUN);
        taskRepository.save(task);
    }

    @Override
    public TaskNotice create(Principal principal, String id, TaskNoticeUpdateCommand command) throws Exception {
        TaskNotice result = save(principal, id, new TaskNotice(), command);
        applicationEvents.fireCurd(principal, TaskNotice.class, result.getId(), EventType.CREATE, command);
        return result;
    }

    private TaskNotice save(Principal principal, String id, TaskNotice o, TaskNoticeUpdateCommand command) {
        if (o.isNoticed() || o.isDeleted()) {
            throw new JNonAuthorizationError();
        }
        Task task = taskRepository.find(id);
        checkUpdateAuth(principal, task);
        o.setTask(task);
        o.setNoticeDateTime(command.getNoticeDateTime());
        o.setContent(command.getContent());
        Collection<Operator> receivers = J.emptyIfNull(command.getReceivers()).stream()
                .map(EntityDTO::getId)
                .map(operatorRepository::find)
                .collect(Collectors.toSet());
        o.setReceivers(receivers);
        return taskNoticeRepository.save(o);
    }

    @Override
    public TaskNotice update(Principal principal, String id, String taskNoticeId, TaskNoticeUpdateCommand command) throws Exception {
        TaskNotice taskNotice = taskNoticeRepository.find(taskNoticeId);
        TaskNotice result = save(principal, id, taskNotice, command);
        applicationEvents.fireCurd(principal, TaskNotice.class, result.getId(), EventType.UPDATE, command);
        return result;
    }

    @Override
    public void deleteTaskNotice(Principal principal, String id, String taskNoticeId) throws Exception {
        final Task task = taskRepository.find(id);
        // checkUpdateAuth(principal, task);
        taskNoticeRepository.delete(taskNoticeId);
        applicationEvents.fireCurd(principal, TaskNotice.class, taskNoticeId, EventType.DELETE);
    }

    @Override
    public TaskFeedback create(Principal principal, String taskId, TaskFeedbackUpdateCommand command) throws Exception {
        Task task = taskRepository.find(taskId);
        TaskFeedback taskFeedback = new TaskFeedback();
        taskFeedback.setTask(task);
        taskFeedback = save(principal, taskFeedback, command);
        applicationEvents.fireCurd(principal, TaskFeedback.class, taskFeedback.getId(), EventType.CREATE, command);
        return taskFeedback;
    }

    private TaskFeedback save(Principal principal, TaskFeedback o, TaskFeedbackUpdateCommand command) throws JsonProcessingException {
        o.setNote(command.getNote());
        o.setFieldsValue(command.getFieldsValue());
        final Collection<UploadFile> attachments = J.emptyIfNull(command.getAttachments())
                .stream()
                .map(EntityDTO::getId)
                .map(uploadFileRepository::find)
                .collect(Collectors.toSet());
        o.setAttachments(attachments);
        if (o.getTask().isManager(principal)) {
            o.setAtAll(command.isAtAll());
            if (command.isAtAll()) {
                o.setAtOperators(null);
            } else {
                final Collection<Operator> atOperators = J.emptyIfNull(command.getAtOperators())
                        .stream()
                        .map(EntityDTO::getId)
                        .map(operatorRepository::find)
                        .collect(Collectors.toSet());
                o.setAtOperators(atOperators);
            }
        }
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return taskFeedbackRepository.save(o);
    }

    @Override
    public TaskFeedback update(Principal principal, String id, TaskFeedbackUpdateCommand command) throws Exception {
        TaskFeedback taskFeedback = save(principal, taskFeedbackRepository.find(id), command);
        applicationEvents.fireCurd(principal, TaskFeedback.class, taskFeedback.getId(), EventType.UPDATE, command);
        return taskFeedback;
    }

    @Override
    public void deleteTaskFeedback(Principal principal, String id) throws Exception {
        TaskFeedback taskFeedback = taskFeedbackRepository.find(id);
        if (taskFeedback.getCreator().getId().equals(principal.getName())) {
            taskFeedbackRepository.delete(id);
            applicationEvents.fireCurd(principal, TaskFeedback.class, taskFeedback.getId(), EventType.DELETE);
        }
    }

    @Override
    public TaskFeedbackComment create(Principal principal, String taskFeedbackId, TaskFeedbackCommentUpdateCommand command) throws Exception {
        TaskFeedbackComment taskFeedbackComment = save(principal, taskFeedbackId, new TaskFeedbackComment(), command);
        applicationEvents.fireCurd(principal, TaskFeedbackComment.class, taskFeedbackComment.getId(), EventType.CREATE, command);
        return taskFeedbackComment;
    }

    private TaskFeedbackComment save(Principal principal, String taskFeedbackId, TaskFeedbackComment o, TaskFeedbackCommentUpdateCommand command) {
        TaskFeedback taskFeedback = taskFeedbackRepository.find(taskFeedbackId);
        o.setTaskFeedback(taskFeedback);
        o.setNote(command.getNote());
        Collection<ExamQuestion> examQuestions = J.emptyIfNull(command.getExamQuestions())
                .stream()
                .map(EntityDTO::getId)
                .map(examQuestionRepository::find)
                .collect(Collectors.toSet());
        o.setExamQuestions(examQuestions);
        Collection<UploadFile> attachments = J.emptyIfNull(command.getAttachments())
                .stream()
                .map(EntityDTO::getId)
                .map(uploadFileRepository::find)
                .collect(Collectors.toSet());
        o.setAttachments(attachments);
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return taskFeedbackCommentRepository.save(o);
    }

    @Override
    public void deleteTaskFeedbackComment(Principal principal, String id) throws Exception {
        TaskFeedbackComment taskFeedbackComment = taskFeedbackCommentRepository.find(id);
        if (!taskFeedbackComment.getCreator().getId().equals(principal.getName())) {
            throw new JNonAuthorizationError();
        }
        taskFeedbackCommentRepository.delete(id);
        applicationEvents.fireCurd(principal, TaskFeedbackComment.class, taskFeedbackComment.getId(), EventType.DELETE);
    }

    @Override
    public void deleteParticipant(Principal principal, String id, String participantId) throws Exception {
        Task task = taskRepository.find(id);
        if (!task.isManager(principal)) {
            throw new JNonAuthorizationError();
        }
        Collection<Operator> participants = task.getParticipants().stream()
                .filter(operator -> !operator.getId().equals(participantId))
                .collect(Collectors.toSet());
        task.setParticipants(participants);
        taskRepository.save(task);
    }

    @Override
    public void deleteAllParticipant(Principal principal, String id) throws Exception {
        Task task = taskRepository.find(id);
        if (!task.isManager(principal)) {
            throw new JNonAuthorizationError();
        }
        task.setParticipants(null);
        taskRepository.save(task);
    }

    @Override
    public void deleteFollower(Principal principal, String id, String followerId) throws Exception {
        Task task = taskRepository.find(id);
        if (!task.getCharger().getId().equals(principal.getName())) {
            throw new JNonAuthorizationError();
        }
        Collection<Operator> followers = task.getFollowers().stream()
                .filter(operator -> !operator.getId().equals(followerId))
                .collect(Collectors.toSet());
        task.setFollowers(followers);
        taskRepository.save(task);
    }

    @Override
    public TaskEvaluate create(Principal principal, String taskId, TaskEvaluateUpdateCommand command) throws JsonProcessingException {
        Task task = taskRepository.find(taskId);
        Operator executor = task.getParticipants()
                .stream()
                .filter(operator -> Objects.equals(operator.getId(), command.getExecutor().getId()))
                .findFirst()
                .orElse(null);
        if (executor == null) {
            throw new JNonAuthorizationError();
        }

        TaskEvaluate taskEvaluate = taskEvaluateRepository.queryByTaskId(taskId)
                .filter(it -> Objects.equals(principal.getName(), it.getCreator().getId())
                        && Objects.equals(command.getExecutor().getId(), it.getExecutor().getId()))
                .findFirst()
                .orElse(new TaskEvaluate());
        taskEvaluate.setTask(task);
        taskEvaluate.setExecutor(executor);
        return save(principal, taskEvaluate, command);
    }

    private TaskEvaluate save(Principal principal, TaskEvaluate o, TaskEvaluateUpdateCommand command) throws JsonProcessingException {
        o.setNote(command.getNote());
        o.setFieldsValue(command.getFieldsValue());
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return taskEvaluateRepository.save(o);
    }

    @Override
    public TaskEvaluate update(Principal principal, String taskEvaluateId, TaskEvaluateUpdateCommand command) throws Exception {
        return save(principal, taskEvaluateRepository.find(taskEvaluateId), command);
    }

    @Override
    public void deleteTaskEvaluate(Principal principal, String taskEvaluateId) throws Exception {
        TaskEvaluate taskEvaluate = taskEvaluateRepository.find(taskEvaluateId);
        if (taskEvaluate.getCreator().getId().equals(principal.getName())) {
            taskEvaluateRepository.delete(taskEvaluateId);
        }
    }

    @Override
    public String getAdUrl(Principal principal, String id) {
        Task task = taskRepository.find(id);
        return Optional.ofNullable(task)
                .map(Task::getPlan)
                .map(Plan::getAdUrl)
                .orElse(Constant.AD_URL);
    }

    @Override
    public void addParticipants(Principal principal, String id, OperatorsUpdateCommand command) throws Exception {
        Task task = taskRepository.find(id);
        checkUpdateAuth(principal, task);
        Collection<Operator> participants = J.emptyIfNull(command.getOperators())
                .stream()
                .map(EntityDTO::getId)
                .distinct()
                .map(operatorRepository::find)
                .collect(Collectors.toSet());
        participants.addAll(J.emptyIfNull(task.getParticipants()));
        task.setParticipants(participants);
        taskRepository.save(task);
    }

    @Override
    public void addFollowers(Principal principal, String id, OperatorsUpdateCommand command) throws Exception {
        Task task = taskRepository.find(id);
        checkUpdateAuth(principal, task);
        Collection<Operator> followers = J.emptyIfNull(command.getOperators())
                .stream()
                .map(EntityDTO::getId)
                .distinct()
                .map(operatorRepository::find)
                .collect(Collectors.toSet());
        followers.addAll(J.emptyIfNull(task.getFollowers()));
        task.setFollowers(followers);
        taskRepository.save(task);
    }

    @Override
    public void updateNickNames(Principal principal, NickNamesUpdateCommand command) throws Exception {
        final Map<String, String> nickNameMap = command.getNickNameMap();
        command.getTasks().stream()
                .map(EntityDTO::getId)
                .map(taskRepository::find)
                .forEach(task -> {
                    task.relateOperators()
                            .filter(it -> nickNameMap.containsKey(it.getId()))
                            .filter(it -> it.isSelf(principal) || task.isManager(principal))
                            .forEach(operator -> {
                                TaskOperatorContextData contextData = taskOperatorContextDataRepository.find(task, operator);
                                contextData.setNickName(nickNameMap.get(operator.getId()));
                                taskOperatorContextDataRepository.save(contextData);
                            });
                });
    }

    @Override
    public void top(Principal principal, String id) {
        final Task task = taskRepository.find(id);
        checkUpdateAuth(principal, task);
        final Operator operator = operatorRepository.find(principal);
        task._loginfo(operator);
        taskRepository.save(task);
    }

    @Override
    public void quit(final Principal principal, String id) {
        final Task task = taskRepository.find(id);
        final Predicate<Operator> predicate = it -> !Objects.equals(it.getId(), principal.getName());
        final Collection<Operator> participants = J.emptyIfNull(task.getParticipants())
                .stream()
                .filter(predicate)
                .collect(Collectors.toSet());
        task.setParticipants(participants);
        final Collection<Operator> followers = J.emptyIfNull(task.getFollowers())
                .stream()
                .filter(predicate)
                .collect(Collectors.toSet());
        task.setFollowers(followers);
        taskRepository.save(task);
    }
}

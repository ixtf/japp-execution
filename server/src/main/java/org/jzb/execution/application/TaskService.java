package org.jzb.execution.application;

import org.jzb.execution.application.command.*;
import org.jzb.execution.domain.*;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface TaskService {

    Task create(Principal principal, TaskUpdateCommand command) throws Exception;

    Task update(Principal principal, String id, TaskUpdateCommand command) throws Exception;

    void updateNickName(Principal principal, String id, String nickName);

    TasksInvite inviteTicket(Principal principal, TaskBatchBaseCommand command) throws Exception;

    TasksFollowInvite followInviteTicket(Principal principal, TasksFollowInviteCommand command) throws Exception;

    void delete(Principal principal, String id);

    Task copy(Principal principal, String id);

    void weixinShareTimeline(Principal principal, String id) throws Exception;

    void finish(Principal principal, String id);

    void restart(Principal principal, String id);

    TaskNotice create(Principal principal, String id, TaskNoticeUpdateCommand command) throws Exception;

    TaskNotice update(Principal principal, String id, String taskNoticeId, TaskNoticeUpdateCommand command) throws Exception;

    void deleteTaskNotice(Principal principal, String id, String taskNoticeId) throws Exception;

    TaskFeedback create(Principal principal, String taskId, TaskFeedbackUpdateCommand command) throws Exception;

    TaskFeedback update(Principal principal, String taskFeedbackId, TaskFeedbackUpdateCommand command) throws Exception;

    void deleteTaskFeedback(Principal principal, String taskFeedbackId) throws Exception;

    TaskFeedbackComment create(Principal principal, String taskFeedbackId, TaskFeedbackCommentUpdateCommand command) throws Exception;

    void deleteTaskFeedbackComment(Principal principal, String id) throws Exception;

    void deleteParticipant(Principal principal, String id, String participantId) throws Exception;

    void deleteFollower(Principal principal, String id, String followerId) throws Exception;

    TaskEvaluate create(Principal principal, String taskId, TaskEvaluateUpdateCommand command) throws Exception;

    TaskEvaluate update(Principal principal, String taskEvaluateId, TaskEvaluateUpdateCommand command) throws Exception;

    void deleteTaskEvaluate(Principal principal, String taskEvaluateId) throws Exception;

    String getAdUrl(Principal principal, String id);

    void addParticipants(Principal principal, String id, OperatorsUpdateCommand command) throws Exception;

    void addFollowers(Principal principal, String id, OperatorsUpdateCommand command) throws Exception;

    void updateNickNames(Principal principal, NickNamesUpdateCommand command) throws Exception;

    void top(Principal principal, String id);

    void quit(Principal principal, String id);
}

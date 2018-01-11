package org.jzb.execution.application;

import org.jzb.execution.application.command.ExamQuestionLabUpdateCommand;
import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.extra.ExamQuestionLabInvite;

import java.security.Principal;

/**
 * Created by jzb on 17-2-27.
 */
public interface ExamQuestionLabService {
    ExamQuestionLab create(Principal principal, ExamQuestionLabUpdateCommand command);

    ExamQuestionLab update(Principal principal, String id, ExamQuestionLabUpdateCommand command);

    void delete(Principal principal, String id) throws Exception;

    ExamQuestionLabInvite inviteTicket(Principal principal, String id) throws Exception;

    void deleteParticipant(Principal principal, String id, String participantId) throws Exception;
}

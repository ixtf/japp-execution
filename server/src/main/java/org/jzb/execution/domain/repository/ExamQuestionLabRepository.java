package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.extra.ExamQuestionLabInvite;

import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface ExamQuestionLabRepository {
    ExamQuestionLab save(ExamQuestionLab lab);

    ExamQuestionLab find(String id);

    void delete(String id);

    Stream<ExamQuestionLab> query(Principal principal);

    ExamQuestionLabInvite save(ExamQuestionLabInvite labInvite);

    ExamQuestionLabInvite findExamQuestionLabInviteByTicket(String ticket);
}

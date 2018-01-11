package org.jzb.execution.application;

import org.jzb.execution.application.command.ExamQuestionUpdateCommand;
import org.jzb.execution.application.command.TaskBatchBaseCommand;
import org.jzb.execution.domain.extra.ExamQuestion;

import java.io.File;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface ExamQuestionService {
    ExamQuestion create(Principal principal, String labId, ExamQuestionUpdateCommand command);

    ExamQuestion update(Principal principal, String labId, String id, ExamQuestionUpdateCommand command);

    void delete(Principal principal, String id);

    File review(Principal principal, TaskBatchBaseCommand command) throws Exception;

    File managerReview(Principal principal, String operatorId, TaskBatchBaseCommand command) throws Exception;
}

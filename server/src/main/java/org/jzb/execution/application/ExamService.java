package org.jzb.execution.application;

import org.jzb.execution.application.command.ExamUpdateCommand;
import org.jzb.execution.domain.extra.Exam;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface ExamService {
    Exam create(Principal principal, ExamUpdateCommand command);

    Exam update(Principal principal, String id, ExamUpdateCommand command);
}

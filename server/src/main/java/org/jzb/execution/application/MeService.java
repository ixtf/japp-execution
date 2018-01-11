package org.jzb.execution.application;

import org.jzb.execution.application.command.ExamQuestionJoinCommand;
import org.jzb.execution.application.command.ManagerExamQuestionJoinCommand;
import org.jzb.execution.application.command.MeUpdateCommand;
import org.jzb.execution.application.command.PasswordChangeCommand;

import java.io.File;
import java.security.Principal;

/**
 * Created by jzb on 17-2-27.
 */
public interface MeService {

    void updateInfo(Principal principal, MeUpdateCommand command);

    void changePassword(Principal principal, PasswordChangeCommand command) throws Exception;

    File joinExamQuestions(Principal principal, ExamQuestionJoinCommand command) throws Exception;

    File managerJoinExamQuestion(Principal principal, ManagerExamQuestionJoinCommand command) throws Exception;

    File getJoinExamQuestions(String fileName);

    void quitTask(Principal principal, String taskId);
}

package org.jzb.execution.application.internal;

import org.apache.commons.io.FileUtils;
import org.jboss.resteasy.annotations.GZIP;
import org.jzb.J;
import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.Util;
import org.jzb.execution.application.MeService;
import org.jzb.execution.application.command.*;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.operator.Login;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jzb.execution.Constant.UPLOAD_TMP_PATH;

/**
 * Created by jzb on 17-4-15.
 */
@GZIP
@Stateless
public class MeServiceImpl implements MeService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private ExamQuestionRepository examQuestionRepository;
    @Inject
    private UploadFileRepository uploadFileRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;

    @Override
    public void updateInfo(Principal principal, MeUpdateCommand command) {
        Operator operator = operatorRepository.find(principal);
        if (J.nonBlank(command.getName())) {
            operator.setName(command.getName());
            operatorRepository.save(operator);
        }
        if (J.nonBlank(command.getMobile())) {
            Login login = operatorRepository.findLoginByLoginId(command.getMobile());
            if (login.getOperator().equals(operator)) {
                login.setLoginId(command.getMobile());
                operatorRepository.save(operator, login);
            }
        }
    }

    @Override
    public void changePassword(Principal principal, PasswordChangeCommand command) throws Exception {
        Login login = operatorRepository.findLogin(principal.getName());
        if (!J.checkPassword(login.getLoginPassword(), command.getOldPassword()))
            throw new JNonAuthorizationError();
        if (!command.getLoginPassword().equals(command.getLoginPasswordAgain()))
            throw new RuntimeException();
        String password = J.password(command.getLoginPassword());
        login.setLoginPassword(password);
        operatorRepository.save(login.getOperator(), login);
    }

    @Override
    public File joinExamQuestions(Principal principal, ExamQuestionJoinCommand command) throws Exception {
        Stream<ExamQuestion> examQuestionStream = command.getExamQuestions()
                .stream()
                .map(EntityDTO::getId)
                .distinct()
                .map(examQuestionRepository::find);
        return Util.joinExamQuestions(examQuestionStream);
    }

    @Override
    public File managerJoinExamQuestion(Principal principal, ManagerExamQuestionJoinCommand command) throws Exception {
        Stream<ExamQuestion> examQuestionStream = command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .map(taskRepository::find)
                .filter(task -> task.isManager(principal))
                .flatMap(taskFeedbackRepository::queryBy)
                .filter(taskFeedback -> Objects.equals(taskFeedback.getCreator().getId(), command.getOperator().getId()))
                .flatMap(taskFeedbackCommentRepository::queryBy)
                .map(TaskFeedbackComment::getExamQuestions)
                .filter(J::nonEmpty)
                .flatMap(it -> it.stream());
        return Util.joinExamQuestions(examQuestionStream);
    }

    @Override
    public File getJoinExamQuestions(String fileName) {
        return FileUtils.getFile(UPLOAD_TMP_PATH, fileName);
    }

    @Override
    public void quitTask(Principal principal, String taskId) {
        Task task = taskRepository.find(taskId);
        final Collection<Operator> participants = J.emptyIfNull(task.getParticipants())
                .stream()
                .filter(it -> !Objects.equals(principal.getName(), it.getId()))
                .collect(Collectors.toSet());
        task.setParticipants(participants);
        taskRepository.save(task);
    }
}

package org.jzb.execution.application.internal;

import org.jzb.J;
import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.Util;
import org.jzb.execution.application.ExamQuestionService;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.application.command.ExamQuestionUpdateCommand;
import org.jzb.execution.application.command.TaskBatchBaseCommand;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.UploadFile;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.*;

import javax.inject.Inject;
import java.io.File;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 描述：
 *
 * @author jzb 2017-10-29
 */
public class ExamQuestionServiceImpl implements ExamQuestionService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private ExamQuestionLabRepository examQuestionLabRepository;
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
    public ExamQuestion create(Principal principal, String labId, ExamQuestionUpdateCommand command) {
        return save(principal, labId, new ExamQuestion(), command);
    }

    private ExamQuestion save(Principal principal, String labId, ExamQuestion o, ExamQuestionUpdateCommand command) {
        final ExamQuestionLab lab = Optional.ofNullable(labId)
                .map(examQuestionLabRepository::find)
                .orElse(null);
        o.setLab(lab);
        o.setName(command.getName());
        UploadFile question = uploadFileRepository.find(command.getQuestion().getId());
        o.setQuestion(question);
        UploadFile answer = uploadFileRepository.find(command.getAnswer().getId());
        o.setAnswer(answer);
        o.setTags(command.getTags());
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return examQuestionRepository.save(o);
    }

    @Override
    public ExamQuestion update(Principal principal, String labId, String examQuestionId, ExamQuestionUpdateCommand command) {
        return save(principal, labId, examQuestionRepository.find(examQuestionId), command);
    }

    @Override
    public void delete(Principal principal, String id) {
        ExamQuestion examQuestion = examQuestionRepository.find(id);
        if (!examQuestion.getLab().isManager(principal)) {
            throw new JNonAuthorizationError();
        }
        examQuestionRepository.delete(id);
    }

    @Override
    public File review(Principal principal, TaskBatchBaseCommand command) throws Exception {
        Stream<ExamQuestion> examQuestionStream = command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .map(taskRepository::find)
                .flatMap(taskFeedbackRepository::queryBy)
                .filter(taskFeedback -> taskFeedback.getCreator().getId().equals(principal.getName()))
                .flatMap(taskFeedbackCommentRepository::queryBy)
                .map(TaskFeedbackComment::getExamQuestions)
                .filter(J::nonEmpty)
                .flatMap(Collection::stream)
                .distinct();
        return Util.joinExamQuestions(examQuestionStream);
    }

    @Override
    public File managerReview(Principal principal, String operatorId, TaskBatchBaseCommand command) throws Exception {
        Stream<ExamQuestion> examQuestionStream = command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .map(taskRepository::find)
                .flatMap(taskFeedbackRepository::queryBy)
                .filter(taskFeedback -> taskFeedback.getCreator().getId().equals(operatorId))
                .flatMap(taskFeedbackCommentRepository::queryBy)
                .map(TaskFeedbackComment::getExamQuestions)
                .filter(J::nonEmpty)
                .flatMap(Collection::stream)
                .distinct();
        return Util.joinExamQuestions(examQuestionStream);
    }
}

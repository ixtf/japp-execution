package org.jzb.execution.application.internal;

import org.jzb.J;
import org.jzb.execution.application.ExamService;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.application.command.ExamUpdateCommand;
import org.jzb.execution.domain.extra.Exam;
import org.jzb.execution.domain.extra.ExamItem;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.ExamQuestionRepository;
import org.jzb.execution.domain.repository.ExamRepository;
import org.jzb.execution.domain.repository.OperatorRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class ExamServiceImpl implements ExamService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private ExamRepository examRepository;
    @Inject
    private ExamQuestionRepository examQuestionRepository;

    @Override
    public Exam create(Principal principal, ExamUpdateCommand command) {
        return save(principal, new Exam(), command);
    }

    private Exam save(Principal principal, Exam o, ExamUpdateCommand command) {
        o.setTitle(command.getTitle());
        AtomicInteger sortBy = new AtomicInteger();
        Collection<ExamItem> items = command.getItems().stream()
                .map(EntityDTO::getId)
                .map(examQuestionRepository::find)
                .map(examQuestion -> {
                    ExamItem item = new ExamItem();
                    item.setId(J.uuid58());
                    item.setExam(o);
                    item.setExamQuestion(examQuestion);
                    item.setSortBy(sortBy.incrementAndGet());
                    return item;
                })
                .collect(Collectors.toSet());
        o.setItems(items);
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return examRepository.save(o);
    }

    @Override
    public Exam update(Principal principal, String id, ExamUpdateCommand command) {
        return save(principal, examRepository.find(id), command);
    }
}

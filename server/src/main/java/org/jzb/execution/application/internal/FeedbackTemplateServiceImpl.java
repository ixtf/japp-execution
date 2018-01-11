package org.jzb.execution.application.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jzb.execution.application.FeedbackTemplateService;
import org.jzb.execution.application.command.FeedbackTemplateUpdateCommand;
import org.jzb.execution.domain.FeedbackTemplate;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.FeedbackTemplateRepository;
import org.jzb.execution.domain.repository.OperatorRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class FeedbackTemplateServiceImpl implements FeedbackTemplateService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private FeedbackTemplateRepository feedbackTemplateRepository;

    @Override
    public FeedbackTemplate create(Principal principal, FeedbackTemplateUpdateCommand command) throws JsonProcessingException {
        return save(principal, new FeedbackTemplate(), command);
    }

    private FeedbackTemplate save(Principal principal, FeedbackTemplate o, FeedbackTemplateUpdateCommand command) throws JsonProcessingException {
        o.setName(command.getName());
        command.getFields().stream().parallel().forEach(field -> field.uuid());
        o.setFields(command.getFields());
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return feedbackTemplateRepository.save(o);
    }

    @Override
    public FeedbackTemplate update(Principal principal, String id, FeedbackTemplateUpdateCommand command) throws JsonProcessingException {
        return save(principal, feedbackTemplateRepository.find(id), command);
    }

    @Override
    public void delete(Principal principal, String id) {
        FeedbackTemplate feedbackTemplate = feedbackTemplateRepository.find(id);
        if (feedbackTemplate.getCreator().getId().equals(principal.getName())) {
            feedbackTemplateRepository.delete(id);
        }
    }
}

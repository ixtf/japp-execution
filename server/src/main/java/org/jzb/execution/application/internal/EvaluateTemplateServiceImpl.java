package org.jzb.execution.application.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jzb.execution.application.EvaluateTemplateService;
import org.jzb.execution.application.command.EvaluateTemplateUpdateCommand;
import org.jzb.execution.domain.EvaluateTemplate;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.EvaluateTemplateRepository;
import org.jzb.execution.domain.repository.OperatorRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class EvaluateTemplateServiceImpl implements EvaluateTemplateService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private EvaluateTemplateRepository evaluateTemplateRepository;

    @Override
    public EvaluateTemplate create(Principal principal, EvaluateTemplateUpdateCommand command) throws JsonProcessingException {
        return save(principal, new EvaluateTemplate(), command);
    }

    private EvaluateTemplate save(Principal principal, EvaluateTemplate o, EvaluateTemplateUpdateCommand command) throws JsonProcessingException {
        o.setName(command.getName());
        command.getFields().stream().parallel().forEach(field -> field.uuid());
        o.setFields(command.getFields());
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return evaluateTemplateRepository.save(o);
    }

    @Override
    public EvaluateTemplate update(Principal principal, String id, EvaluateTemplateUpdateCommand command) throws JsonProcessingException {
        return save(principal, evaluateTemplateRepository.find(id), command);
    }

    @Override
    public void delete(Principal principal, String id) {
        EvaluateTemplate evaluateTemplate = evaluateTemplateRepository.find(id);
        if (evaluateTemplate.getCreator().getId().equals(principal.getName())) {
            evaluateTemplateRepository.delete(id);
        }
    }
}

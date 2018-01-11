package org.jzb.execution.application;

import org.jzb.execution.application.command.EvaluateTemplateUpdateCommand;
import org.jzb.execution.domain.EvaluateTemplate;

import java.security.Principal;

/**
 * Created by jzb on 17-2-27.
 */
public interface EvaluateTemplateService {
    EvaluateTemplate create(Principal principal, EvaluateTemplateUpdateCommand command) throws Exception;

    EvaluateTemplate update(Principal principal, String id, EvaluateTemplateUpdateCommand command) throws Exception;

    void delete(Principal principal, String id);
}

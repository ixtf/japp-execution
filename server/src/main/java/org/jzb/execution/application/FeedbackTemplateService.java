package org.jzb.execution.application;

import org.jzb.execution.application.command.FeedbackTemplateUpdateCommand;
import org.jzb.execution.domain.FeedbackTemplate;

import java.security.Principal;

/**
 * Created by jzb on 17-2-27.
 */
public interface FeedbackTemplateService {
    FeedbackTemplate create(Principal principal, FeedbackTemplateUpdateCommand command) throws Exception;

    FeedbackTemplate update(Principal principal, String id, FeedbackTemplateUpdateCommand command) throws Exception;

    void delete(Principal principal, String id);
}

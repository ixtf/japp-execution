package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.FeedbackTemplate;

import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface FeedbackTemplateRepository {

    FeedbackTemplate save(FeedbackTemplate feedbackTemplate);

    FeedbackTemplate find(String id);

    Stream<FeedbackTemplate> query(Principal principal);

    void delete(String id);
}

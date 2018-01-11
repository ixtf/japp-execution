package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.FeedbackTemplate;
import org.jzb.execution.domain.repository.FeedbackTemplateRepository;

import javax.enterprise.context.ApplicationScoped;
import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaFeedbackTemplateRepository extends JpaCURDRepository<FeedbackTemplate, String> implements FeedbackTemplateRepository {

    @Override
    public Stream<FeedbackTemplate> query(Principal principal) {
        return em.createNamedQuery("FeedbackTemplate.queryByCreatorId", FeedbackTemplate.class)
                .setParameter("creatorId", principal.getName())
                .getResultList()
                .stream();
    }
}

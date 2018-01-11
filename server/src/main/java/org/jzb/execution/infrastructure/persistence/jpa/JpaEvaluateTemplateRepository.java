package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.EvaluateTemplate;
import org.jzb.execution.domain.repository.EvaluateTemplateRepository;

import javax.enterprise.context.ApplicationScoped;
import java.security.Principal;
import java.util.Collection;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaEvaluateTemplateRepository extends JpaCURDRepository<EvaluateTemplate, String> implements EvaluateTemplateRepository {

    @Override
    public Collection<EvaluateTemplate> query(Principal principal) {
        return em.createNamedQuery("EvaluateTemplate.queryByCreatorId", EvaluateTemplate.class)
                .setParameter("creatorId", principal.getName())
                .getResultList();
    }
}

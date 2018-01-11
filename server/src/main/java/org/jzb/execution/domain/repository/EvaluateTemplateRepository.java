package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.EvaluateTemplate;

import java.security.Principal;
import java.util.Collection;

/**
 * Created by jzb on 17-2-27.
 */
public interface EvaluateTemplateRepository {

    EvaluateTemplate save(EvaluateTemplate evaluateTemplate);

    EvaluateTemplate find(String id);

    Collection<EvaluateTemplate> query(Principal principal);

    void delete(String id);
}

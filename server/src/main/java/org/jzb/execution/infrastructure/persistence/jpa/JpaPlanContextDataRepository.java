package org.jzb.execution.infrastructure.persistence.jpa;


import org.apache.commons.lang3.StringUtils;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.PlanContextData;
import org.jzb.execution.domain.repository.PlanContextDataRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaPlanContextDataRepository extends JpaCURDRepository<PlanContextData, String> implements PlanContextDataRepository {
    @Override
    public PlanContextData save(PlanContextData entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(entity.getPlan().getId());
        }
        return em.merge(entity);
    }

    @Override
    public PlanContextData find(Plan plan) {
        PlanContextData result = find(plan.getId());
        return result == null ? save(new PlanContextData(plan)) : result;
    }

    @Override
    public void addDownloadCount(Plan plan) {
        em.createNamedQuery("PlanContextData.addDownloadCount")
                .setParameter("plan", plan)
                .executeUpdate();
    }
}

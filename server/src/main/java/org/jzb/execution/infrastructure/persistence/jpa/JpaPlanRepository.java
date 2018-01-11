package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.application.query.PlanQuery;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.PlanInvite;
import org.jzb.execution.domain.repository.PlanRepository;
import org.jzb.execution.infrastructure.persistence.lucene.PlanLucene;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaPlanRepository extends JpaCURDRepository<Plan, String> implements PlanRepository {
    @Resource
    private ManagedScheduledExecutorService ses;
    @Inject
    private PlanLucene planLucene;

    @Override
    public Plan save(Plan o) {
        Plan result = super.save(o);
        planLucene.index(result);
        return result;
    }

    @Override
    public void query(PlanQuery command) throws Exception {
        planLucene.query(command);
    }

    @Override
    public PlanInvite save(PlanInvite invite) {
        return em.merge(invite);
    }

    @Override
    public PlanInvite findPlanInviteByTicket(String ticket) {
        return em.find(PlanInvite.class, ticket);
    }

}

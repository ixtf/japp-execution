package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.PlanFollow;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.PlanFollowRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaPlanFollowRepository extends JpaCURDRepository<PlanFollow, String> implements PlanFollowRepository {
    @Override
    public PlanFollow save(Plan plan, Operator follower) {
        String id = PlanFollow.generateId(plan, follower);
        return Optional.ofNullable(find(id))
                .orElseGet(() -> {
                    em.createNamedQuery("PlanContextData.addFollowCount")
                            .setParameter("plan", plan)
                            .executeUpdate();
                    em.createNamedQuery("ChannelContextData.addFollowCount")
                            .setParameter("channel", plan.getChannel())
                            .executeUpdate();
                    PlanFollow planFollow = new PlanFollow(plan, follower);
                    planFollow.setFollowDateTime(new Date());
                    return em.merge(planFollow);
                });
    }

    @Override
    public void delete(Plan plan, Operator follower) {
        String id = PlanFollow.generateId(plan, follower);
        Optional.ofNullable(find(id)).ifPresent(planFollow -> {
            em.createNamedQuery("PlanContextData.decrementFollowCount")
                    .setParameter("plan", plan)
                    .executeUpdate();
            em.remove(planFollow);
        });
    }

    @Override
    public long countByPlanId(String planId) {
        return em.createNamedQuery("PlanFollow.countByPlanId", Long.class)
                .setParameter("planId", planId)
                .getSingleResult();
    }

    @Override
    public Stream<PlanFollow> queryByPlanId(String planId, int first, int pageSize) {
        return em.createNamedQuery("PlanFollow.queryByPlanId", PlanFollow.class)
                .setParameter("planId", planId)
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .getResultList()
                .stream();
    }
}

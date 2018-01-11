package org.jzb.execution.domain.repository;


import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.PlanFollow;
import org.jzb.execution.domain.operator.Operator;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface PlanFollowRepository {

    PlanFollow find(String id);

    PlanFollow save(Plan plan, Operator follower);

    void delete(Plan plan, Operator follower);

    long countByPlanId(String id);

    Stream<PlanFollow> queryByPlanId(String id, int first, int pageSize);
}

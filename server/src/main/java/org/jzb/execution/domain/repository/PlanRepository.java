package org.jzb.execution.domain.repository;

import org.jzb.execution.application.query.PlanQuery;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.PlanInvite;

/**
 * Created by jzb on 17-2-27.
 */
public interface PlanRepository {
    Plan save(Plan plan);

    Plan find(String id);

    void delete(String id);

    void query(PlanQuery command) throws Exception;

    PlanInvite save(PlanInvite planInvite);

    PlanInvite findPlanInviteByTicket(String ticket);
}

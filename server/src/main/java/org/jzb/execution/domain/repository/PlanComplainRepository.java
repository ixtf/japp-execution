package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.PlanComplain;

/**
 * Created by jzb on 17-2-27.
 */
public interface PlanComplainRepository {
    PlanComplain save(PlanComplain planComplain);

    PlanComplain find(String id);

    void delete(String id);

}

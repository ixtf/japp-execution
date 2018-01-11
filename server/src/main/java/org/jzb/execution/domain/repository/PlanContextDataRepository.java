package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.PlanContextData;

/**
 * Created by jzb on 17-2-27.
 */
public interface PlanContextDataRepository {
    PlanContextData save(PlanContextData o);

    PlanContextData find(Plan plan);

    void addDownloadCount(Plan plan);
}

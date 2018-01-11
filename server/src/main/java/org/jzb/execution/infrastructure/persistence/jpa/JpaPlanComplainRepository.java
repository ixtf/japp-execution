package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.PlanComplain;
import org.jzb.execution.domain.repository.PlanComplainRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaPlanComplainRepository extends JpaCURDRepository<PlanComplain, String> implements PlanComplainRepository {

}

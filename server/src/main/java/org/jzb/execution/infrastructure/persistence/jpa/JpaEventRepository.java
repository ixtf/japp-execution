package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.event.EventEntity;
import org.jzb.execution.domain.repository.EventRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by jzb on 17-4-15.
 */
@ApplicationScoped
public class JpaEventRepository extends JpaCURDRepository<EventEntity, String> implements EventRepository {
}
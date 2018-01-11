package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.event.EventEntity;

/**
 * Created by jzb on 17-4-15.
 */
public interface EventRepository<T extends EventEntity> {
    T save(T event) throws Exception;
}

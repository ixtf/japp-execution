package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.J;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskContextData;
import org.jzb.execution.domain.repository.TaskContextDataRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskContextDataRepository extends JpaCURDRepository<TaskContextData, String> implements TaskContextDataRepository {
    @Override
    public TaskContextData save(TaskContextData entity) {
        if (J.isBlank(entity.getId())) {
            entity.setId(entity.getTask().getId());
        }
        return em.merge(entity);
    }

    @Override
    public TaskContextData find(Task task) {
        TaskContextData result = find(task.getId());
        return result == null ? save(new TaskContextData(task)) : result;
    }
}

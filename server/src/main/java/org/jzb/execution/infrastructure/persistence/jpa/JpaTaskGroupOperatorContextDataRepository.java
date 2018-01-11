package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.J;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.TaskGroupOperatorContextData;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.TaskGroupOperatorContextDataRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskGroupOperatorContextDataRepository extends JpaCURDRepository<TaskGroupOperatorContextData, String> implements TaskGroupOperatorContextDataRepository {
    @Override
    public TaskGroupOperatorContextData save(TaskGroupOperatorContextData entity) {
        if (J.isBlank(entity.getId())) {
            String id = TaskGroupOperatorContextData.generateId(entity.getTaskGroup(), entity.getOperator());
            entity.setId(id);
        }
        return em.merge(entity);
    }

    @Override
    public TaskGroupOperatorContextData find(TaskGroup taskGroup, Operator operator) {
        String id = TaskGroupOperatorContextData.generateId(taskGroup, operator);
        TaskGroupOperatorContextData result = find(id);
        return result == null ? save(new TaskGroupOperatorContextData(taskGroup, operator)) : result;
    }

}

package org.jzb.execution.infrastructure.persistence.jpa;


import org.jzb.J;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskOperatorContextData;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.TaskOperatorContextDataRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskOperatorContextDataRepository extends JpaCURDRepository<TaskOperatorContextData, String> implements TaskOperatorContextDataRepository {
    @Override
    public TaskOperatorContextData save(TaskOperatorContextData entity) {
        if (J.isBlank(entity.getId())) {
            String id = TaskOperatorContextData.generateId(entity.getTask(), entity.getOperator());
            entity.setId(id);
        }
        return em.merge(entity);
    }

    @Override
    public TaskOperatorContextData find(Task task, Operator operator) {
        String id = TaskOperatorContextData.generateId(task, operator);
        TaskOperatorContextData result = find(id);
        return result == null ? save(new TaskOperatorContextData(task, operator)) : result;
    }

    @Override
    public Stream<TaskOperatorContextData> queryBy(Task task) {
        return em.createNamedQuery("TaskOperatorContextData.queryByTask", TaskOperatorContextData.class)
                .setParameter("task", task)
                .getResultList()
                .stream();
    }
}

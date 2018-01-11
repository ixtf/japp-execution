package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.TaskEvaluate;
import org.jzb.execution.domain.repository.TaskEvaluateRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskEvaluateRepository extends JpaCURDRepository<TaskEvaluate, String> implements TaskEvaluateRepository {
    @Override
    public Stream<TaskEvaluate> queryByTaskId(String taskId) {
        return em.createNamedQuery("TaskEvaluate.queryByTaskId", TaskEvaluate.class)
                .setParameter("taskId", taskId)
                .getResultList()
                .stream();
    }
}

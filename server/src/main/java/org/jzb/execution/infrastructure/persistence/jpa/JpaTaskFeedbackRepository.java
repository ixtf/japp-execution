package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskFeedback;
import org.jzb.execution.domain.repository.TaskFeedbackRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskFeedbackRepository extends JpaCURDRepository<TaskFeedback, String> implements TaskFeedbackRepository {
    @Override
    public Stream<TaskFeedback> queryByTaskId(String taskId) {
        return em.createNamedQuery("TaskFeedback.queryByTaskId", TaskFeedback.class)
                .setParameter("taskId", taskId)
                .getResultList()
                .stream();
    }

    @Override
    public Stream<TaskFeedback> queryBy(Task task) {
        return queryByTaskId(task.getId());
    }
}

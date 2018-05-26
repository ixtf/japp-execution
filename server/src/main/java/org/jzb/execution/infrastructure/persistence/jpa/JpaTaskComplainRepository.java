package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.TaskComplain;
import org.jzb.execution.domain.repository.TaskComplainRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskComplainRepository extends JpaCURDRepository<TaskComplain, String> implements TaskComplainRepository {

    @Override
    public Stream<TaskComplain> queryByTaskId(String taskId) {
        return em.createNamedQuery("TaskComplain.queryByTaskId", TaskComplain.class)
                .setParameter("taskId", taskId)
                .getResultList()
                .stream();
    }
}

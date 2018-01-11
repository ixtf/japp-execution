package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.TaskNotice;
import org.jzb.execution.domain.repository.TaskNoticeRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskNoticeRepository extends JpaCURDRepository<TaskNotice, String> implements TaskNoticeRepository {
    @Override
    public Stream<TaskNotice> queryByTaskId(String taskId) {
        return em.createNamedQuery("TaskNotice.queryByTaskId", TaskNotice.class)
                .setParameter("taskId", taskId)
                .getResultList()
                .stream();
    }
}

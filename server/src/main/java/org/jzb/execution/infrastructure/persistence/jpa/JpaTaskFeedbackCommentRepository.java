package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.TaskFeedback;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.repository.TaskFeedbackCommentRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskFeedbackCommentRepository extends JpaCURDRepository<TaskFeedbackComment, String> implements TaskFeedbackCommentRepository {
    @Override
    public Stream<TaskFeedbackComment> queryByTaskFeedbackId(String taskFeedbackId) {
        return queryBy(em.find(TaskFeedback.class, taskFeedbackId));
    }

    @Override
    public Stream<TaskFeedbackComment> queryBy(TaskFeedback taskFeedback) {
        return em.createNamedQuery("TaskFeedbackComment.queryByTaskFeedback", TaskFeedbackComment.class)
                .setParameter("taskFeedback", taskFeedback)
                .getResultList()
                .stream();
    }
}

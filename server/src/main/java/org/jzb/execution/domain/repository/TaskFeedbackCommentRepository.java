package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.TaskFeedback;
import org.jzb.execution.domain.TaskFeedbackComment;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskFeedbackCommentRepository {
    TaskFeedbackComment save(TaskFeedbackComment taskFeedbackComment);

    TaskFeedbackComment find(String id);

    void delete(String id);

    Stream<TaskFeedbackComment> queryByTaskFeedbackId(String taskFeedbackId);

    Stream<TaskFeedbackComment> queryBy(TaskFeedback taskFeedback);
}

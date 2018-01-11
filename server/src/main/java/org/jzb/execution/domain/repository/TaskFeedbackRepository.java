package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskFeedback;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskFeedbackRepository {
    TaskFeedback save(TaskFeedback taskFeedback);

    TaskFeedback find(String id);

    void delete(String id);

    Stream<TaskFeedback> queryByTaskId(String taskId);

    Stream<TaskFeedback> queryBy(Task task);
}

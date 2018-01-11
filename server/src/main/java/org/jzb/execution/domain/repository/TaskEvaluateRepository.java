package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.TaskEvaluate;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskEvaluateRepository {
    TaskEvaluate save(TaskEvaluate taskEvaluate);

    TaskEvaluate find(String id);

    void delete(String id);

    Stream<TaskEvaluate> queryByTaskId(String taskId);
}

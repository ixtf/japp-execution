package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.TaskComplain;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskComplainRepository {
    TaskComplain save(TaskComplain taskComplain);

    TaskComplain find(String id);

    void delete(String id);

    Stream<TaskComplain> queryByTaskId(String taskId);
}

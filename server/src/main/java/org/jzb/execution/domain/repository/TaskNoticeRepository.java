package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.TaskNotice;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskNoticeRepository {
    TaskNotice save(TaskNotice taskNotice);

    TaskNotice find(String id);

    void delete(String id);

    Stream<TaskNotice> queryByTaskId(String taskId);
}

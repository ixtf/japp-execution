package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskContextData;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskContextDataRepository {
    TaskContextData save(TaskContextData taskContextData);

    TaskContextData find(Task task);
}

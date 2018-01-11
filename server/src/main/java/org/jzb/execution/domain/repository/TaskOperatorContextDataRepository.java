package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskOperatorContextData;
import org.jzb.execution.domain.operator.Operator;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskOperatorContextDataRepository {
    TaskOperatorContextData save(TaskOperatorContextData taskOperatorContextData);

    TaskOperatorContextData find(Task task, Operator operator);

    Stream<TaskOperatorContextData> queryBy(Task task);
}

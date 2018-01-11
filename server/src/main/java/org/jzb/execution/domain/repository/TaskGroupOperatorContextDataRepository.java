package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.TaskGroupOperatorContextData;
import org.jzb.execution.domain.operator.Operator;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskGroupOperatorContextDataRepository {
    TaskGroupOperatorContextData save(TaskGroupOperatorContextData taskGroupOperatorContextData);

    TaskGroupOperatorContextData find(TaskGroup taskGroup, Operator operator);
}

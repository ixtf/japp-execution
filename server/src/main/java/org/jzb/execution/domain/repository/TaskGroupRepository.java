package org.jzb.execution.domain.repository;


import org.jzb.execution.domain.TaskGroup;

import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskGroupRepository {
    TaskGroup save(TaskGroup taskGroup);

    TaskGroup find(String id);

    Stream<TaskGroup> query(Principal principal) throws Exception;
}

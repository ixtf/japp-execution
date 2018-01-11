package org.jzb.execution.domain.repository;


import org.apache.lucene.search.Query;
import org.jzb.execution.application.query.TaskQuery;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TasksFollowInvite;
import org.jzb.execution.domain.TasksInvite;

import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface TaskRepository {
    Task save(Task task);

    Task find(String id);

    void delete(String id);

    TasksInvite save(TasksInvite tasksInvite);

    TasksInvite findTasksInviteByTicket(String ticket);

    TasksFollowInvite save(TasksFollowInvite tasksFollowInvite);

    TasksFollowInvite findTasksFollowInviteByTicket(String ticket);

    Stream<Task> query(Principal principal) throws Exception;

    Stream<Task> queryByTaskGroupId(Principal principal, String taskGroupId) throws Exception;

    void query(TaskQuery principal) throws Exception;

    Stream<Task> query(Query query) throws Exception;
}

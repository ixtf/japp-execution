package org.jzb.execution.infrastructure.persistence.jpa;

import org.apache.lucene.search.Query;
import org.jzb.execution.application.query.TaskQuery;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TasksFollowInvite;
import org.jzb.execution.domain.TasksInvite;
import org.jzb.execution.domain.repository.TaskRepository;
import org.jzb.execution.infrastructure.persistence.lucene.TaskLucene;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskRepository extends JpaCURDRepository<Task, String> implements TaskRepository {
    @Inject
    private TaskLucene taskLucene;

    @Override
    public Task save(Task o) {
        Task result = super.save(o);
        taskLucene.index(result);
        return result;
    }

    @Override
    public void delete(String id) {
        taskLucene.delete(id);
        super.delete(id);
    }

    @Override
    public TasksInvite save(TasksInvite invite) {
        return em.merge(invite);
    }

    @Override
    public TasksInvite findTasksInviteByTicket(String ticket) {
        return em.find(TasksInvite.class, ticket);
    }

    @Override
    public TasksFollowInvite save(TasksFollowInvite invite) {
        return em.merge(invite);
    }

    @Override
    public TasksFollowInvite findTasksFollowInviteByTicket(String ticket) {
        return em.find(TasksFollowInvite.class, ticket);
    }

    @Override
    public Stream<Task> query(Principal principal) throws Exception {
        return taskLucene.query(principal);
    }

    @Override
    public Stream<Task> queryByTaskGroupId(Principal principal, String taskGroupId) throws Exception {
        return taskLucene.queryByTaskGroupId(principal, taskGroupId);
    }

    @Override
    public void query(TaskQuery command) throws Exception {
        taskLucene.query(command);
    }

    @Override
    public Stream<Task> query(Query query) throws Exception {
        return taskLucene.query(query);
    }

}

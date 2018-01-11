package org.jzb.execution.application.query;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.jzb.ee.JEE;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.repository.TaskRepository;
import org.jzb.search.JQuery;

import java.security.Principal;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 描述：
 *
 * @author jzb 2017-11-01
 */
public class TaskGroupQuery extends JQuery<Stream<TaskGroup>> {
    private BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
    private Boolean isCreate;
    private Boolean isManage;
    private Boolean isParticipant;

    public TaskGroupQuery(Principal principal) {
        super(principal);
        bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
    }

    public TaskGroupQuery exe() throws Exception {
        final TaskRepository taskRepository = JEE.getBean(TaskRepository.class);
        this.result = taskRepository.query(bqBuilder.build())
                .filter(task -> {
                    if (isCreate != null) {
                        final boolean b = task.getModifier().getId().equals(principal.getName());
                        if (!isCreate.equals(b)) {
                            return false;
                        }
                    }
                    return true;
                })
                .map(Task::getTaskGroup)
                .filter(Objects::nonNull);
        return this;
    }

    public TaskGroupQuery isCreate(boolean b) {
        isCreate = b;
        return this;
    }

    public TaskGroupQuery isManage(boolean b) {
        isManage = b;
        return this;
    }

    public TaskGroupQuery isParticipant(boolean b) {
        isParticipant = b;
        return this;
    }
}

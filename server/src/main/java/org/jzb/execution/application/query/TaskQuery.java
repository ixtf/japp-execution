package org.jzb.execution.application.query;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.execution.domain.repository.TaskRepository;
import org.jzb.search.JPageQuery;

import java.security.Principal;

/**
 * Created by jzb on 17-4-23.
 */
public class TaskQuery extends JPageQuery<JsonNode> {
    private String q;
    private boolean isHistory;

    public TaskQuery(Principal principal, int first, Integer pageSize) {
        super(principal, first, pageSize);
    }

    public TaskQuery exe(TaskRepository taskRepository) throws Exception {
        taskRepository.query(this);
        return this;
    }

    public TaskQuery q(String q) {
        this.q = q;
        return this;
    }

    public TaskQuery isHistory(boolean isHistory) {
        this.isHistory = isHistory;
        return this;
    }

    public String q() {
        return this.q;
    }

    public boolean isHistory() {
        return this.isHistory;
    }

}

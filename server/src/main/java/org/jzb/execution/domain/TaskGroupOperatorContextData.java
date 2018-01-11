/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jzb.J;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKOPERATORCONTEXTDATA")
@NamedQueries({
        @NamedQuery(name = "TaskOperatorContextData.queryByTask", query = "SELECT o FROM TaskOperatorContextData o WHERE o.task=:task")
})
public class TaskGroupOperatorContextData extends AbstractEntity {
    @JsonIgnore
    @ManyToOne
    private TaskGroup taskGroup;
    @ManyToOne
    private Operator operator;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date sortBy;

    public TaskGroupOperatorContextData(TaskGroup taskGroup, Operator operator) {
        this.taskGroup = taskGroup;
        this.operator = operator;
        this.id = generateId(taskGroup, operator);
    }

    public TaskGroupOperatorContextData() {
    }

    public static String generateId(TaskGroup taskGroup, Operator operator) {
        return J.uuid58(taskGroup.getId(), operator.getId());
    }

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Date getSortBy() {
        return sortBy;
    }

    public void setSortBy(Date sortBy) {
        this.sortBy = sortBy;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKSFOLLOWINVITE")
public class TasksFollowInvite extends WeixinInvite {
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "T_TASKSFOLLOWINVITE_T_TASK")
    @Size(min = 1)
    private Collection<Task> tasks;

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }
}

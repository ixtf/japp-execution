/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import org.jzb.J;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_PLANFOLLOW")
@NamedQueries({
        @NamedQuery(name = "PlanFollow.countByPlanId", query = "SELECT COUNT(o) FROM PlanFollow o WHERE o.plan.id=:planId"),
        @NamedQuery(name = "PlanFollow.queryByPlanId", query = "SELECT o FROM PlanFollow o WHERE o.plan.id=:planId"),
})
public class PlanFollow extends AbstractEntity {
    @ManyToOne
    private Plan plan;
    @ManyToOne
    private Operator follower;
    @Temporal(TemporalType.TIMESTAMP)
    private Date followDateTime;

    public PlanFollow(Plan plan, Operator follower) {
        this.plan = plan;
        this.follower = follower;
        this.id = generateId(plan, follower);
    }

    public PlanFollow() {
    }

    public static String generateId(Plan plan, Operator follower) {
        return J.uuid58(plan.getId(), follower.getId());
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Operator getFollower() {
        return follower;
    }

    public void setFollower(Operator follower) {
        this.follower = follower;
    }

    public Date getFollowDateTime() {
        return followDateTime;
    }

    public void setFollowDateTime(Date followDateTime) {
        this.followDateTime = followDateTime;
    }
}

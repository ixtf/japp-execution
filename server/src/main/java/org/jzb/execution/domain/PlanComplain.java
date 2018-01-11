/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_PLANCOMPLAIN")
@NamedQueries({
        @NamedQuery(name = "PlanComplain.queryByPlanId", query = "SELECT o FROM PlanComplain o WHERE o.plan.id=:planId")
})
public class PlanComplain extends AbstractLogable {
    @ManyToOne
    @NotNull
    private Plan plan;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String content;

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

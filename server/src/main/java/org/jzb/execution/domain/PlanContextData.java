/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_PLANCONTEXTDATA")
@NamedQueries({
        @NamedQuery(name = "PlanContextData.addFollowCount", query = "UPDATE PlanContextData o SET o.followCount=o.followCount+1 WHERE o.plan=:plan"),
        @NamedQuery(name = "PlanContextData.decrementFollowCount", query = "UPDATE PlanContextData o SET o.followCount=o.followCount-1 WHERE o.plan=:plan"),
        @NamedQuery(name = "PlanContextData.addDownloadCount", query = "UPDATE PlanContextData o SET o.downloadCount=o.downloadCount+1 WHERE o.plan=:plan"),
        @NamedQuery(name = "PlanContextData.addReadCount", query = "UPDATE PlanContextData o SET o.readCount=o.readCount+1 WHERE o.plan=:plan"),
        @NamedQuery(name = "PlanContextData.addComplainCount", query = "UPDATE PlanContextData o SET o.complainCount=o.complainCount+1 WHERE o.plan=:plan"),
})
public class PlanContextData extends AbstractLogable {
    @JsonIgnore
    @PrimaryKeyJoinColumn
    @OneToOne
    private Plan plan;
    private int followCount;
    private int downloadCount;
    private int readCount;
    private int complainCount;

    public PlanContextData(Plan plan) {
        this.plan = plan;
        this.id = plan.getId();
    }

    public PlanContextData() {
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getComplainCount() {
        return complainCount;
    }

    public void setComplainCount(int complainCount) {
        this.complainCount = complainCount;
    }
}

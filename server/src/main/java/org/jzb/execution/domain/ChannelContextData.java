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
@Table(name = "T_CHANNELCONTEXTDATA")
@NamedQueries({
        @NamedQuery(name = "ChannelContextData.addPlanCount", query = "UPDATE ChannelContextData o SET o.planCount=o.planCount+1 WHERE o.channel=:channel"),
        @NamedQuery(name = "ChannelContextData.addFollowCount", query = "UPDATE ChannelContextData o SET o.followCount=o.followCount+1 WHERE o.channel=:channel"),
})
public class ChannelContextData extends AbstractEntity {
    @JsonIgnore
    @OneToOne
    @PrimaryKeyJoinColumn
    private Channel channel;
    private int planCount;
    private int followCount;

    public ChannelContextData(Channel channel) {
        this.channel = channel;
        this.id = channel.getId();
    }

    public ChannelContextData() {
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getPlanCount() {
        return planCount;
    }

    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }
}

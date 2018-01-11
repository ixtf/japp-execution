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
@Table(name = "T_CHANNELFOLLOW")
@NamedQueries({
        @NamedQuery(name = "ChannelFollow.countByChannelId", query = "SELECT COUNT(o) FROM ChannelFollow o WHERE o.channel.id=:channelId"),
        @NamedQuery(name = "ChannelFollow.queryByChannelId", query = "SELECT o FROM ChannelFollow o WHERE o.channel.id=:channelId"),
})
public class ChannelFollow extends AbstractEntity {
    @ManyToOne
    private Channel channel;
    @ManyToOne
    private Operator follower;
    @Temporal(TemporalType.TIMESTAMP)
    private Date followDateTime;

    public ChannelFollow(Channel channel, Operator follower) {
        this.channel = channel;
        this.follower = follower;
        this.id = generateId(channel, follower);
    }

    public ChannelFollow() {
    }

    public static String generateId(Channel channel, Operator follower) {
        return J.uuid58(channel.getId(), follower.getId());
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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

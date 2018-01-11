package org.jzb.execution.domain.repository;


import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.ChannelFollow;
import org.jzb.execution.domain.operator.Operator;

import java.util.Collection;

/**
 * Created by jzb on 17-2-27.
 */
public interface ChannelFollowRepository {

    ChannelFollow find(String id);

    ChannelFollow save(Channel channel, Operator follower);

    long countByChannelId(String id);

    Collection<ChannelFollow> queryByChannelId(String id, int first, int pageSize);

}

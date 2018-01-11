package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.ChannelContextData;

/**
 * Created by jzb on 17-2-27.
 */
public interface ChannelContextDataRepository {
    ChannelContextData save(ChannelContextData o);

    ChannelContextData find(Channel channel);

    void addPlanCount(Channel channel);
}

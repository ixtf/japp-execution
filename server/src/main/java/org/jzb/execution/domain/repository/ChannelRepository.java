package org.jzb.execution.domain.repository;


import org.jzb.execution.domain.Channel;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface ChannelRepository {
    Channel find(String id);

    Channel save(Channel channel);

    Stream<Channel> findAll();

    void delete(String id);
}

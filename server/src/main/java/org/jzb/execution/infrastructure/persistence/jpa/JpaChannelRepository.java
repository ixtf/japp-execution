package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.repository.ChannelRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaChannelRepository extends JpaCURDRepository<Channel, String> implements ChannelRepository {
}

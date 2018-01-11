package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.ChannelFollow;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.ChannelFollowRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaChannelFollowRepository extends JpaCURDRepository<ChannelFollow, String> implements ChannelFollowRepository {
    @Override
    public ChannelFollow save(Channel channel, Operator follower) {
        String id = ChannelFollow.generateId(channel, follower);
        return Optional.ofNullable(find(id))
                .orElseGet(() -> {
                    em.createNamedQuery("ChannelContextData.addFollowCount")
                            .setParameter("channel", channel)
                            .executeUpdate();
                    ChannelFollow channelFollow = new ChannelFollow(channel, follower);
                    channelFollow.setFollowDateTime(new Date());
                    return em.merge(channelFollow);
                });
    }

    @Override
    public long countByChannelId(String id) {
        return em.createNamedQuery("ChannelFollow.countByChannelId", Long.class)
                .setParameter("channelId", id)
                .getSingleResult();
    }

    @Override
    public Collection<ChannelFollow> queryByChannelId(String id, int first, int pageSize) {
        return em.createNamedQuery("ChannelFollow.queryByChannelId", ChannelFollow.class)
                .setParameter("channelId", id)
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .getResultList();
    }
}

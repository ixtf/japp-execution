package org.jzb.execution.infrastructure.persistence.jpa;


import org.jzb.J;
import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.ChannelContextData;
import org.jzb.execution.domain.repository.ChannelContextDataRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaChannelContextDataRepository extends JpaCURDRepository<ChannelContextData, String> implements ChannelContextDataRepository {
    @Override
    public ChannelContextData save(ChannelContextData entity) {
        if (J.isBlank(entity.getId())) {
            entity.setId(entity.getChannel().getId());
        }
        return em.merge(entity);
    }

    @Override
    public ChannelContextData find(Channel channel) {
        return Optional.ofNullable(find(channel.getId()))
                .orElseGet(() -> save(new ChannelContextData(channel)));
    }

    @Override
    public void addPlanCount(Channel channel) {
        em.createNamedQuery("ChannelContextData.addPlanCount")
                .setParameter("channel", channel)
                .executeUpdate();
    }
}

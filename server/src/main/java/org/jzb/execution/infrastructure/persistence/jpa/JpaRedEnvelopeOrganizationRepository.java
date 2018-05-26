package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.RedEnvelopeOrganization;
import org.jzb.execution.domain.RedEnvelopeOrganizationInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.RedEnvelopeOrganizationRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaRedEnvelopeOrganizationRepository extends JpaCURDRepository<RedEnvelopeOrganization, String> implements RedEnvelopeOrganizationRepository {
    @Override
    public Stream<RedEnvelopeOrganization> queryByManager(Operator manager) {
        return em.createNamedQuery("RedEnvelopeOrganization.queryByManager", RedEnvelopeOrganization.class)
                .setParameter("manager", manager)
                .getResultList()
                .stream();
    }

    @Override
    public RedEnvelopeOrganizationInvite save(RedEnvelopeOrganizationInvite invite) {
        return em.merge(invite);
    }

    @Override
    public RedEnvelopeOrganizationInvite findRedEnvelopeOrganizationInviteByTicket(String ticket) {
        return em.find(RedEnvelopeOrganizationInvite.class, ticket);
    }

}

package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.Enlist;
import org.jzb.execution.domain.EnlistFeedback;
import org.jzb.execution.domain.EnlistInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.EnlistRepository;
import org.jzb.execution.infrastructure.persistence.lucene.EnlistLucene;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaEnlistRepository extends JpaCURDRepository<Enlist, String> implements EnlistRepository {
    @Inject
    private EnlistLucene enlistLucene;

    @Override
    public Enlist save(Enlist o) {
        Enlist result = super.save(o);
        enlistLucene.index(result);
        return result;
    }

    @Override
    public void delete(String id) {
        enlistLucene.delete(id);
        super.delete(id);
    }

    @Override
    public Stream<Enlist> query(Principal principal) {
        final Operator operator = em.find(Operator.class, principal.getName());
        final Stream<Enlist> stream1 = em.createNamedQuery("Enlist.queryByManager", Enlist.class)
                .setParameter("manager", operator)
                .getResultList()
                .stream();
        final Stream<Enlist> stream2 = em.createNamedQuery("EnlistFeedback.queryByCreator", EnlistFeedback.class)
                .setParameter("creator", operator)
                .getResultList()
                .stream()
                .map(EnlistFeedback::getEnlist);
        return Stream.concat(stream1, stream2).distinct();
    }

    @Override
    public EnlistInvite save(EnlistInvite invite) {
        return em.merge(invite);
    }

    @Override
    public EnlistInvite findEnlistInviteByTicket(String ticket) {
        return em.find(EnlistInvite.class, ticket);
    }
}

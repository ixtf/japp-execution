package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.RedEnvelope;
import org.jzb.execution.domain.RedEnvelopeStrategy;
import org.jzb.execution.domain.repository.RedEnvelopeRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaRedEnvelopeRepository extends JpaCURDRepository<RedEnvelope, String> implements RedEnvelopeRepository {

    @Override
    public Stream<RedEnvelope> queryBy(RedEnvelopeStrategy redEnvelopeStrategy) {
        // return em.createNamedQuery("RedEnvelope.queryByRedEnvelopeStrategy", RedEnvelope.class)
        //         .setParameter("redEnvelopeStrategy", redEnvelopeStrategy)
        //         .getResultStream();
        return Stream.empty();
    }
}

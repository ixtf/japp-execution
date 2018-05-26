package org.jzb.execution.domain.repository;


import org.jzb.execution.domain.RedEnvelope;
import org.jzb.execution.domain.RedEnvelopeStrategy;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface RedEnvelopeRepository {

    RedEnvelope find(String id);

    RedEnvelope save(RedEnvelope redEnvelope);

    Stream<RedEnvelope> queryBy(RedEnvelopeStrategy redEnvelopeStrategy);
}

package org.jzb.execution.application;

import org.jzb.execution.domain.RedEnvelope;
import org.jzb.execution.domain.RedEnvelopeStrategy;

import java.security.Principal;

/**
 * Created by jzb on 17-7-6.
 */
public interface RedEnvelopeService {
    RedEnvelope create(Principal principal, RedEnvelopeStrategy redEnvelopeStrategy);
}

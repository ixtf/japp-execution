package org.jzb.execution.application.internal;

import org.apache.commons.lang3.RandomUtils;
import org.jzb.execution.application.RedEnvelopeService;
import org.jzb.execution.domain.RedEnvelope;
import org.jzb.execution.domain.RedEnvelopeStrategy;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.RedEnvelopeRepository;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.security.Principal;

/**
 * 描述：
 *
 * @author jzb 2018-04-03
 */
@Singleton
public class RedEnvelopeServiceImpl implements RedEnvelopeService {
    @Inject
    private RedEnvelopeRepository redEnvelopeRepository;
    @Inject
    private OperatorRepository operatorRepository;

    @Override
    public RedEnvelope create(Principal principal, RedEnvelopeStrategy redEnvelopeStrategy) {
        redEnvelopeRepository.queryBy(redEnvelopeStrategy);
        RedEnvelope redEnvelope = new RedEnvelope();
        redEnvelope.setRedEnvelopeStrategy(redEnvelopeStrategy);
        final double random = RandomUtils.nextDouble(redEnvelopeStrategy.getMin().doubleValue(), redEnvelopeStrategy.getMax().doubleValue());
        final BigDecimal amount = BigDecimal.valueOf(random);
        redEnvelope.setAmount(amount);
        final Operator operator = operatorRepository.find(principal);
        redEnvelope._loginfo(operator);
        return redEnvelopeRepository.save(redEnvelope);
    }

}

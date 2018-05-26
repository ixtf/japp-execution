package org.jzb.execution.domain.repository;


import org.jzb.execution.domain.RedEnvelopeOrganization;
import org.jzb.execution.domain.RedEnvelopeOrganizationInvite;
import org.jzb.execution.domain.operator.Operator;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface RedEnvelopeOrganizationRepository {
    RedEnvelopeOrganization save(RedEnvelopeOrganization redEnvelopeOrganization);

    RedEnvelopeOrganization find(String id);

    void delete(String id);

    Stream<RedEnvelopeOrganization> findAll();

    Stream<RedEnvelopeOrganization> queryByManager(Operator manager);

    RedEnvelopeOrganizationInvite save(RedEnvelopeOrganizationInvite invite);

    RedEnvelopeOrganizationInvite findRedEnvelopeOrganizationInviteByTicket(String ticket);
}

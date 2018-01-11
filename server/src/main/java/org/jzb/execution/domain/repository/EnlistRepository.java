package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.Enlist;
import org.jzb.execution.domain.EnlistInvite;

import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface EnlistRepository {
    Enlist save(Enlist enlist);

    Enlist find(String id);

    void delete(String id);

    Stream<Enlist> query(Principal principal);

    EnlistInvite save(EnlistInvite invite);

    EnlistInvite findEnlistInviteByTicket(String ticket);
}

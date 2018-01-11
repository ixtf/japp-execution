package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.execution.domain.Enlist;
import org.jzb.execution.domain.EnlistFeedback;
import org.jzb.execution.domain.repository.EnlistFeedbackRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaEnlistFeedbackRepository extends JpaCURDRepository<EnlistFeedback, String> implements EnlistFeedbackRepository {
    @Override
    public Stream<EnlistFeedback> queryBy(Enlist enlist) {
        return em.createNamedQuery("EnlistFeedback.queryByEnlist")
                .setParameter("enlist", enlist)
                .getResultList()
                .stream();
    }
}

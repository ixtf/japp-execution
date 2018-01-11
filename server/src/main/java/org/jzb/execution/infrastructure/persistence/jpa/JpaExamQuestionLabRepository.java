package org.jzb.execution.infrastructure.persistence.jpa;


import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.extra.ExamQuestionLabInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.ExamQuestionLabRepository;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.infrastructure.persistence.lucene.ExamQuestionLucene;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Principal;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaExamQuestionLabRepository extends JpaCURDRepository<ExamQuestionLab, String> implements ExamQuestionLabRepository {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private ExamQuestionLucene examQuestionLucene;

    @Override
    public Stream<ExamQuestionLab> query(Principal principal) {
        Operator operator = operatorRepository.find(principal);
        Stream<ExamQuestionLab> stream1 = em.createNamedQuery("ExamQuestionLab.queryByParticipant", ExamQuestionLab.class)
                .setParameter("participant", operator)
                .getResultList()
                .stream();
        final Stream<ExamQuestionLab> stream2 = em.createNamedQuery("ExamQuestionLab.queryByCreator", ExamQuestionLab.class)
                .setParameter("creator", operator)
                .getResultList()
                .stream();
        return Stream.concat(stream1, stream2);
    }

    @Override
    public ExamQuestionLabInvite save(ExamQuestionLabInvite labInvite) {
        return em.merge(labInvite);
    }

    @Override
    public ExamQuestionLabInvite findExamQuestionLabInviteByTicket(String ticket) {
        return em.find(ExamQuestionLabInvite.class, ticket);
    }
}

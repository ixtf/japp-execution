package org.jzb.execution.infrastructure.persistence.jpa;

import org.jzb.J;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.repository.ExamQuestionRepository;
import org.jzb.execution.infrastructure.persistence.lucene.ExamQuestionLucene;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaExamQuestionRepository extends JpaCURDRepository<ExamQuestion, String> implements ExamQuestionRepository {
    @Inject
    private ExamQuestionLucene examQuestionLucene;

    @Override
    public ExamQuestion save(ExamQuestion o) {
        ExamQuestion result = super.save(o);
        examQuestionLucene.index(result);
        return result;
    }

    @Override
    public long countBy(ExamQuestionLab lab) {
        return em.createNamedQuery("ExamQuestion.countByLab", Long.class)
                .setParameter("lab", lab)
                .getSingleResult();
    }

    @Override
    public Collection<ExamQuestion> queryByLabId(Principal principal, String labId) {
        if ("0".equals(labId) || J.isBlank(labId)) {
            return em.createNamedQuery("ExamQuestion.queryByMyLab", ExamQuestion.class)
                    .setParameter("creatorId", principal.getName())
                    .getResultList();
        }
        return em.createNamedQuery("ExamQuestion.queryByLabId", ExamQuestion.class)
                .setParameter("labId", labId)
                .getResultList();
    }
}

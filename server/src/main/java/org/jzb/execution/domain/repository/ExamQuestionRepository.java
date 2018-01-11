package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.extra.ExamQuestionLab;

import java.security.Principal;
import java.util.Collection;

/**
 * Created by jzb on 17-2-27.
 */
public interface ExamQuestionRepository {
    ExamQuestion save(ExamQuestion examQuestion);

    void delete(String id);

    ExamQuestion find(String id);

    long countBy(ExamQuestionLab examQuestionLab);

    Collection<ExamQuestion> queryByLabId(Principal principal, String examQuestionLabId);
}

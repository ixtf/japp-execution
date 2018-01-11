package org.jzb.execution.infrastructure.persistence.jpa;


import org.jzb.execution.domain.extra.Exam;
import org.jzb.execution.domain.repository.ExamRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaExamRepository extends JpaCURDRepository<Exam, String> implements ExamRepository {
}

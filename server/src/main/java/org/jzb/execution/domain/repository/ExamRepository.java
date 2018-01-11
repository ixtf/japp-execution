package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.extra.Exam;

/**
 * Created by jzb on 17-2-27.
 */
public interface ExamRepository {
    Exam save(Exam exam);

    Exam find(String id);
}

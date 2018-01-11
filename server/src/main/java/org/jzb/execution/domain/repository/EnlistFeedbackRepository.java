package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.Enlist;
import org.jzb.execution.domain.EnlistFeedback;

import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
public interface EnlistFeedbackRepository {
    EnlistFeedback save(EnlistFeedback enlistFeedback);

    EnlistFeedback find(String id);

    void delete(String id);

    Stream<EnlistFeedback> queryBy(Enlist enlist);
}

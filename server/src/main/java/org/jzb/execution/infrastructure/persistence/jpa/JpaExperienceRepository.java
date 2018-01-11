package org.jzb.execution.infrastructure.persistence.jpa;


import org.jzb.execution.domain.Experience;
import org.jzb.execution.domain.repository.ExperienceRepository;
import org.jzb.execution.infrastructure.persistence.lucene.ExperienceLucene;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaExperienceRepository extends JpaCURDRepository<Experience, String> implements ExperienceRepository {
    @Inject
    private ExperienceLucene experienceLucene;

    @Override
    public Experience save(Experience o) {
        Experience result = super.save(o);
        experienceLucene.index(result);
        return result;
    }

}

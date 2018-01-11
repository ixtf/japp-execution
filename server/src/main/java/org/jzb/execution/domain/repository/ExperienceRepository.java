package org.jzb.execution.domain.repository;


import org.jzb.execution.domain.Experience;

/**
 * Created by jzb on 17-2-27.
 */
public interface ExperienceRepository {
    Experience save(Experience experience);

    Experience find(String id);

    void delete(String id);

}

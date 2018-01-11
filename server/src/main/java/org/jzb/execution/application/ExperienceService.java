package org.jzb.execution.application;

import org.jzb.execution.application.command.ExperienceUpdateCommand;
import org.jzb.execution.domain.Experience;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface ExperienceService {
    Experience create(Principal principal, ExperienceUpdateCommand command);

    Experience update(Principal principal, String id, ExperienceUpdateCommand command);
}

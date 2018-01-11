package org.jzb.execution.application.internal;

import org.jzb.execution.application.ExperienceService;
import org.jzb.execution.application.command.ExperienceUpdateCommand;
import org.jzb.execution.domain.Experience;
import org.jzb.execution.domain.repository.ExperienceRepository;
import org.jzb.execution.domain.repository.OperatorRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class ExperienceServiceImpl implements ExperienceService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private ExperienceRepository experienceRepository;

    @Override
    public Experience create(Principal principal, ExperienceUpdateCommand command) {
        return save(principal, new Experience(), command);
    }

    private Experience save(Principal principal, Experience o, ExperienceUpdateCommand command) {
//        o.setName(command.getName());
//        Collection<ExamQuestion> items = command.getItems().stream()
//                .parallel()
//                .map(EntityDTO::getId)
//                .map(examQuestionRepository::find)
//                .collect(Collectors.toSet());
//        o.setItems(items);
//        return examRepository.save(principal, o);
        throw new RuntimeException();
    }

    @Override
    public Experience update(Principal principal, String id, ExperienceUpdateCommand command) {
        return save(principal, experienceRepository.find(id), command);
    }
}

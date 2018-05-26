package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.TaskComplainService;
import org.jzb.execution.application.command.TaskComplainUpdateCommand;
import org.jzb.execution.domain.TaskComplain;
import org.jzb.execution.domain.repository.TaskComplainRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("taskComplains")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskComplainResource {
    @Inject
    private TaskComplainRepository taskComplainRepository;
    @Inject
    private TaskComplainService taskComplainService;

    @POST
    public TaskComplain create(@Context SecurityContext sc,
                               @Valid @NotBlank @QueryParam("taskId") String taskId,
                               @Valid @NotNull TaskComplainUpdateCommand command) throws Exception {
        return taskComplainService.create(sc.getUserPrincipal(), taskId, command);
    }

    @Path("{id}")
    @PUT
    public TaskComplain update(@Context SecurityContext sc,
                               @Valid @NotBlank @PathParam("id") String id,
                               @Valid @NotNull TaskComplainUpdateCommand command) throws Exception {
        return taskComplainService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public TaskComplain get(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return taskComplainRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskComplainService.delete(sc.getUserPrincipal(), id);
    }

    @GET
    public Collection<TaskComplain> list(@Context SecurityContext sc, @Valid @NotBlank @QueryParam("taskId") String taskId) throws Exception {
        return taskComplainRepository.queryByTaskId(taskId).collect(Collectors.toList());
    }

}

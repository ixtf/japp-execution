package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.TaskService;
import org.jzb.execution.application.command.TaskEvaluateUpdateCommand;
import org.jzb.execution.domain.TaskEvaluate;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("taskEvaluates")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskEvaluateResource {
    @Inject
    private TaskService taskService;

    @Path("{id}")
    @PUT
    public Response update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull TaskEvaluateUpdateCommand command) throws Exception {
        TaskEvaluate result = taskService.update(sc.getUserPrincipal(), id, command);
        return Response.ok(result).build();
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskService.deleteTaskEvaluate(sc.getUserPrincipal(), id);
    }

}

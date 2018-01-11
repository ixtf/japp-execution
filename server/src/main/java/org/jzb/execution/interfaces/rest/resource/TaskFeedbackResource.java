package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.TaskService;
import org.jzb.execution.application.command.TaskFeedbackCommentUpdateCommand;
import org.jzb.execution.application.command.TaskFeedbackUpdateCommand;
import org.jzb.execution.domain.TaskFeedback;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.repository.TaskFeedbackCommentRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("taskFeedbacks")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskFeedbackResource {
    @Inject
    private TaskService taskService;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;

    @Path("{id}")
    @PUT
    public Response update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull TaskFeedbackUpdateCommand command) throws Exception {
        TaskFeedback result = taskService.update(sc.getUserPrincipal(), id, command);
        return Response.ok(result).build();
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskService.deleteTaskFeedback(sc.getUserPrincipal(), id);
    }

    @Path("{id}/taskFeedbackComments")
    @POST
    public TaskFeedbackComment create(@Context SecurityContext sc,
                                      @Valid @NotBlank @PathParam("id") String id,
                                      @Valid @NotNull TaskFeedbackCommentUpdateCommand command) throws Exception {
        return taskService.create(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}/taskFeedbackComments")
    @GET
    public Collection<TaskFeedbackComment> list(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        return taskFeedbackCommentRepository.queryByTaskFeedbackId(id).collect(Collectors.toList());
    }

}

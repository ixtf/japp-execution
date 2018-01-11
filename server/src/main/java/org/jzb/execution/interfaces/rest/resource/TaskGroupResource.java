package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.validator.constraints.NotBlank;
import org.jboss.resteasy.annotations.GZIP;
import org.jzb.execution.Util;
import org.jzb.execution.application.TaskGroupService;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.application.command.TaskGroupUpdateCommand;
import org.jzb.execution.application.command.TasksUpdateCommand;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.repository.TaskGroupRepository;
import org.jzb.execution.domain.repository.TaskRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@GZIP
@Path("taskGroups")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskGroupResource {
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskGroupRepository taskGroupRepository;
    @Inject
    private TaskGroupService taskGroupService;

    @POST
    public TaskGroup create(@Context SecurityContext sc, @Valid @NotNull TaskGroupUpdateCommand command) {
        return taskGroupService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public TaskGroup update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull TaskGroupUpdateCommand command) {
        return taskGroupService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public TaskGroup get(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        return taskGroupRepository.find(id);
    }

    @GET
    public Collection<TaskGroup> list(@Context SecurityContext sc) throws Exception {
        return taskGroupRepository.query(sc.getUserPrincipal()).collect(Collectors.toList());
    }

    @Path("{id}/top")
    @PUT
    public void top(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskGroupService.top(sc.getUserPrincipal(), id);
    }

    @Path("{id}/tasks")
    @GET
    public Collection<Task> tasks(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return taskRepository.queryByTaskGroupId(sc.getUserPrincipal(), id)
                .sorted()
                .collect(Collectors.toList());
    }

    @Path("/{id}/weixinShareTimelineToken")
    @GET
    public Response weixinShareTimelineToken(@Context SecurityContext sc,
                                             @Valid @NotBlank @PathParam("id") String id) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        TaskGroup taskGroup = taskGroupRepository.find(id);
        final Set<Task> tasks = taskRepository.queryByTaskGroupId(principal, taskGroup.getId())
                .collect(Collectors.toSet());
        String token = Util.shareToken(principal, taskGroup, tasks);
        JsonNode result = MAPPER.createObjectNode().put("token", token);
        return Response.ok(result).build();
    }

    @Path("/{id}/weixinShareTimelineToken")
    @POST
    public Response weixinShareTimelineToken(@Context SecurityContext sc,
                                             @Valid @NotBlank @PathParam("id") String id,
                                             @NotNull TasksUpdateCommand command) throws Exception {
        TaskGroup taskGroup = taskGroupRepository.find(id);
        Collection<Task> tasks = command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .map(taskRepository::find)
                .collect(Collectors.toSet());
        String token = Util.shareToken(sc.getUserPrincipal(), taskGroup, tasks);
        JsonNode result = MAPPER.createObjectNode().put("token", token);
        return Response.ok(result).build();
    }
}

package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.validator.constraints.NotBlank;
import org.jboss.resteasy.annotations.GZIP;
import org.jzb.execution.Util;
import org.jzb.execution.application.ExamQuestionService;
import org.jzb.execution.application.TaskService;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.application.command.NickNamesUpdateCommand;
import org.jzb.execution.application.command.TaskBatchBaseCommand;
import org.jzb.execution.application.command.TasksFollowInviteCommand;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.TasksFollowInvite;
import org.jzb.execution.domain.TasksInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.TaskRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@GZIP
@Path("taskBatch")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskBatchResource {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskService taskService;
    @Inject
    private ExamQuestionService examQuestionService;

    @Path("taskGroups")
    @GET
    public Collection<TaskGroup> list(@Context SecurityContext sc) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskRepository.query(principal)
                .filter(task -> task.isManager(principal))
                .map(Task::getTaskGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Path("taskGroups/{taskGroupId}/tasks")
    @GET
    public Collection<Task> manageTasks(@Context SecurityContext sc,
                                        @Valid @NotBlank @PathParam("taskGroupId") String taskGroupId) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskRepository.queryByTaskGroupId(principal, taskGroupId)
                .filter(task -> task.isManager(principal))
                .collect(Collectors.toList());
    }

    @Path("nickNames")
    @POST
    public void updateNickNames(@Context SecurityContext sc, @Valid @NotNull NickNamesUpdateCommand command) throws Exception {
        taskService.updateNickNames(sc.getUserPrincipal(), command);
    }

    @Path("finish")
    @POST
    public void finish(@Context SecurityContext sc, @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .distinct()
                .forEach(id -> taskService.finish(principal, id));
    }

    @Path("invite")
    @POST
    public TasksInvite invite(@Context SecurityContext sc, @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        return taskService.inviteTicket(sc.getUserPrincipal(), command);
    }

    @Path("followInvite")
    @POST
    public TasksFollowInvite followInvite(@Context SecurityContext sc, @Valid @NotNull TasksFollowInviteCommand command) throws Exception {
        return taskService.followInviteTicket(sc.getUserPrincipal(), command);
    }

    @Path("joinExamQuestions")
    @POST
    public Response joinExamQuestions(@Context SecurityContext sc, @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        File file = examQuestionService.review(principal, command);
        File pdfFile = Util.toPdf(file);
        Operator operator = operatorRepository.find(principal);
        String downloadName = operator.getName() + "—【" + LocalDate.now().toString() + "】错题.pdf";
        JsonNode result = MAPPER.createObjectNode()
                .put("fileName", downloadName)
                .put("downloadToken", Util.downloadToken(principal, pdfFile, "application/pdf", downloadName))
                .put("html", Util.toHtml(file));
        return Response.ok(result).build();
    }

    @Path("managerJoinExamQuestion")
    @POST
    public Response joinExamQuestions(@Context SecurityContext sc,
                                      @Valid @NotBlank @QueryParam("operatorId") String operatorId,
                                      @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        File file = examQuestionService.managerReview(principal, operatorId, command);
        File pdfFile = Util.toPdf(file);
        Operator operator = operatorRepository.find(operatorId);
        String downloadName = operator.getName() + "—【" + LocalDate.now().toString() + "】错题.pdf";
        // String downloadName = "试题—答案【" + LocalDate.now().toString() + "】错题.pdf";
        JsonNode result = MAPPER.createObjectNode()
                .put("fileName", downloadName)
                .put("downloadToken", Util.downloadToken(principal, pdfFile, "application/pdf", downloadName))
                .put("html", Util.toHtml(file));
        return Response.ok(result).build();
    }

}

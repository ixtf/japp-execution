package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;
import org.jzb.execution.Util;
import org.jzb.execution.application.MeService;
import org.jzb.execution.application.command.*;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskFeedback;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.*;

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
@Path("me")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class MeResource {
    @Inject
    private ExamQuestionRepository examQuestionRepository;
    @Inject
    private MeService meService;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskGroupRepository taskGroupRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;

    /**
     * 自己创建的任务组
     */
    @Path("taskGroups")
    @GET
    public Collection<TaskGroup> taskGroups(@Context SecurityContext sc) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskGroupRepository.query(principal)
                .filter(taskGroup -> taskGroup.getModifier().getId().equals(principal.getName()))
                .collect(Collectors.toSet());
    }

    /**
     * 自己参与的任务组
     * 只要在此任务组下有自己参与的任务，
     * 并且有自己反馈，有管理人的回复，
     * 错题下载使用
     */
    @Path("participantTaskGroups")
    @GET
    public Collection<TaskGroup> participantTaskGroups(@Context SecurityContext sc) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskRepository.query(principal)
                .filter(task -> task.getTaskGroup() != null)
                .filter(task -> task.isParticipant(principal))
                .filter(task -> taskFeedbackRepository.queryBy(task)
                        .filter(it -> it.getCreator().getId().equals(principal.getName()))
                        .flatMap(taskFeedbackCommentRepository::queryBy)
                        .map(TaskFeedbackComment::getExamQuestions)
                        .filter(J::nonEmpty)
                        .findFirst()
                        .isPresent()
                )
                .map(Task::getTaskGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Path("participantTaskGroups/{taskGroupId}/tasks")
    @GET
    public Collection<Task> participantTasks(@Context SecurityContext sc, @Valid @NotBlank @PathParam("taskGroupId") String taskGroupId) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskRepository.query(principal)
                .filter(task -> task.getTaskGroup() != null && task.getTaskGroup().getId().equals(taskGroupId))
                .filter(task -> task.isParticipant(principal))
                .filter(task -> taskFeedbackRepository.queryBy(task)
                        .filter(it -> it.getCreator().getId().equals(principal.getName()))
                        .flatMap(taskFeedbackCommentRepository::queryBy)
                        .map(TaskFeedbackComment::getExamQuestions)
                        .filter(J::nonEmpty)
                        .findFirst()
                        .isPresent()
                )
                .collect(Collectors.toSet());
    }

    /**
     * 自己管理的任务组
     * 只要在此任务组下有自己管理的任务，就算
     */
    @Path("manageTaskGroups")
    @GET
    public Collection<TaskGroup> manageTaskGroups(@Context SecurityContext sc) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskRepository.query(principal)
                .filter(task -> task.getTaskGroup() != null)
                .filter(task -> task.isManager(principal))
                .filter(task -> taskFeedbackRepository.queryBy(task)
                        .flatMap(taskFeedbackCommentRepository::queryBy)
                        .map(TaskFeedbackComment::getExamQuestions)
                        .filter(J::nonEmpty)
                        .findFirst()
                        .isPresent()
                )
                .map(Task::getTaskGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Path("manageTaskGroups/{taskGroupId}/tasks")
    @GET
    public Collection<Task> manageTasks(@Context SecurityContext sc, @Valid @NotBlank @PathParam("taskGroupId") String taskGroupId) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskRepository.query(principal)
                .filter(task -> task.getTaskGroup() != null && task.getTaskGroup().getId().equals(taskGroupId))
                .filter(task -> task.isManager(principal))
                .filter(task -> taskFeedbackRepository.queryBy(task)
                        .flatMap(taskFeedbackCommentRepository::queryBy)
                        .map(TaskFeedbackComment::getExamQuestions)
                        .filter(J::nonEmpty)
                        .findFirst()
                        .isPresent()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Path("manageTaskGroups/operators")
    @POST
    public Collection<Operator> managerOperators(@Context SecurityContext sc,
                                                 @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .distinct()
                .map(taskRepository::find)
                .filter(task -> task.getTaskGroup() != null)
                .filter(task -> task.isManager(principal))
                .flatMap(taskFeedbackRepository::queryBy)
                .filter(taskFeedback -> taskFeedbackCommentRepository.queryBy(taskFeedback)
                        .map(TaskFeedbackComment::getExamQuestions)
                        .filter(J::nonEmpty)
                        .findFirst()
                        .isPresent()
                )
                .map(TaskFeedback::getCreator)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @PUT
    public void update(@Context SecurityContext sc, @Valid @NotNull MeUpdateCommand command) throws Exception {
        meService.updateInfo(sc.getUserPrincipal(), command);
    }

    @Path("tasks/{taskId}")
    @DELETE
    public void quitTask(@Context SecurityContext sc, @Valid @NotBlank @PathParam("taskId") String taskId) throws Exception {
        meService.quitTask(sc.getUserPrincipal(), taskId);
    }

    @Path("joinExamQuestions")
    @POST
    public Response joinExamQuestions(@Context SecurityContext sc, @Valid @NotNull ExamQuestionJoinCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        File file = meService.joinExamQuestions(principal, command);
        File pdfFile = Util.toPdf(file);
        String downloadName = "试题—答案【" + LocalDate.now().toString() + "】.pdf";
        JsonNode result = MAPPER.createObjectNode()
                .put("fileName", file.getName())
                .put("downloadToken", Util.downloadToken(principal, pdfFile, "application/pdf", downloadName))
                .put("html", Util.toHtml(file));
        return Response.ok(result).build();
    }

    @Path("managerJoinExamQuestion")
    @POST
    public Response joinExamQuestions(@Context SecurityContext sc, @Valid @NotNull ManagerExamQuestionJoinCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        File file = meService.managerJoinExamQuestion(principal, command);
        File pdfFile = Util.toPdf(file);
        String downloadName = "试题—答案【" + LocalDate.now().toString() + "】.pdf";
        JsonNode result = MAPPER.createObjectNode()
                .put("fileName", file.getName())
                .put("downloadToken", Util.downloadToken(principal, pdfFile, "application/pdf", downloadName))
                .put("html", Util.toHtml(file));
        return Response.ok(result).build();
    }

}

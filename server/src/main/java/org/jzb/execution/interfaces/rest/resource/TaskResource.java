package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;
import org.jzb.execution.Util;
import org.jzb.execution.application.ApplicationEvents;
import org.jzb.execution.application.TaskService;
import org.jzb.execution.application.command.*;
import org.jzb.execution.application.query.TaskQuery;
import org.jzb.execution.domain.*;
import org.jzb.execution.domain.event.EventType;
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
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@Path("tasks")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskResource {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskContextDataRepository taskContextDataRepository;
    @Inject
    private TaskOperatorContextDataRepository taskOperatorContextDataRepository;
    @Inject
    private TaskNoticeRepository taskNoticeRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;
    @Inject
    private TaskEvaluateRepository taskEvaluateRepository;
    @Inject
    private TaskService taskService;
    @Inject
    private ApplicationEvents applicationEvents;
    @Inject
    private TaskComplainRepository taskComplainRepository;

    @POST
    public Task create(@Context SecurityContext sc, @Valid @NotNull TaskUpdateCommand command) throws Exception {
        return taskService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public Task update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull TaskUpdateCommand command) throws Exception {
        return taskService.update(sc.getUserPrincipal(), id, command);
    }

    @GET
    public JsonNode list(@Context SecurityContext sc) throws Exception {
        return new TaskQuery(sc.getUserPrincipal(), 0, Integer.MAX_VALUE)
                .exe(taskRepository)
                .result;
    }

    @Path("{id}")
    @GET
    public Response update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        Task result = taskRepository.find(id);
        return Response.ok(result).build();
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        taskService.delete(sc.getUserPrincipal(), id);
    }

    @Path("{id}/participants")
    @PUT
    public void addParticipants(@Context SecurityContext sc,
                                @Valid @NotBlank @PathParam("id") String id,
                                @Valid @NotNull OperatorsUpdateCommand command) throws Exception {
        taskService.addParticipants(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}/participants/{participantId}")
    @DELETE
    public void deleteParticipant(@Context SecurityContext sc,
                                  @Valid @NotBlank @PathParam("id") String id,
                                  @Valid @NotBlank @PathParam("participantId") String participantId) throws Exception {
        taskService.deleteParticipant(sc.getUserPrincipal(), id, participantId);
    }

    @Path("{id}/followers")
    @PUT
    public void addFollowers(@Context SecurityContext sc,
                             @Valid @NotBlank @PathParam("id") String id,
                             @Valid @NotNull OperatorsUpdateCommand command) throws Exception {
        taskService.addFollowers(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}/followers/{followerId}")
    @DELETE
    public void deleteFollower(@Context SecurityContext sc,
                               @Valid @NotBlank @PathParam("id") String id,
                               @Valid @NotBlank @PathParam("followerId") String followerId) throws Exception {
        taskService.deleteFollower(sc.getUserPrincipal(), id, followerId);
    }

    @Path("{id}/adUrl")
    @GET
    public JsonNode adUrl(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        String adUrl = taskService.getAdUrl(sc.getUserPrincipal(), id);
        return MAPPER.createObjectNode().put("data", adUrl);
    }

    @Path("/{id}/weixinShareTimelineToken")
    @GET
    public JsonNode weixinShareTimelineToken(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        Task task = taskRepository.find(id);
        String token = Util.shareToken(sc.getUserPrincipal(), task);
        return MAPPER.createObjectNode().put("token", token);
    }

    @Path("{id}/copy")
    @PUT
    public Task copy(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return taskService.copy(sc.getUserPrincipal(), id);
    }

    @Path("{id}/weixinShareTimeline")
    @PUT
    public void weixinShareTimeline(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskService.weixinShareTimeline(sc.getUserPrincipal(), id);
    }

    @Path("{id}/quit")
    @PUT
    public void quit(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskService.quit(sc.getUserPrincipal(), id);
    }

    @Path("{id}/finish")
    @PUT
    public void finish(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskService.finish(sc.getUserPrincipal(), id);
    }

    @Path("{id}/finish")
    @DELETE
    public void restart(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskService.restart(sc.getUserPrincipal(), id);
    }

    @Path("{id}/read")
    @PUT
    public void read(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        applicationEvents.fireCurd(sc.getUserPrincipal(), Task.class, id, EventType.READ);
    }

    @Path("{id}/top")
    @PUT
    public void top(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskService.top(sc.getUserPrincipal(), id);
    }

    @Path("{id}/nickName/{nickName}")
    @PUT
    public void nickName(@Context SecurityContext sc,
                         @Valid @NotBlank @PathParam("id") String id,
                         @Valid @NotBlank @PathParam("nickName") String nickName) throws Exception {
        taskService.updateNickName(sc.getUserPrincipal(), id, nickName);
    }

    @Path("{id}/contextData")
    @GET
    public TaskContextData contextData(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        Task task = taskRepository.find(id);
        final TaskContextData result = taskContextDataRepository.find(task);
        // fixme 反馈数记录
        final long count = taskFeedbackRepository.queryByTaskId(id).count();
        result.setTaskFeedbackCount((int) count);
        return result;
    }

    @Path("{id}/taskOperatorContextData")
    @GET
    public Response taskOperatorContextDataExtra(@Context SecurityContext sc,
                                                 @Valid @NotBlank @PathParam("id") String id,
                                                 @QueryParam("operatorId") String operatorId) throws Exception {
        operatorId = StringUtils.isNoneBlank(operatorId) ? operatorId : sc.getUserPrincipal().getName();
        return taskOperatorContextData(sc, id, operatorId);
    }

    @Path("{id}/operators/{operatorId}/taskOperatorContextData")
    @GET
    public Response taskOperatorContextData(@Context SecurityContext sc,
                                            @Valid @NotBlank @PathParam("id") String id,
                                            @Valid @NotBlank @PathParam("operatorId") String operatorId) throws Exception {
        Task task = taskRepository.find(id);
        Operator operator = operatorRepository.find(operatorId);
        TaskOperatorContextData result = taskOperatorContextDataRepository.find(task, operator);
        return Response.ok(result).build();
    }

    @Path("{id}/taskNotices")
    @POST
    public Response create(@Context SecurityContext sc,
                           @Valid @NotBlank @PathParam("id") String id,
                           @Valid @NotNull TaskNoticeUpdateCommand command) throws Exception {
        TaskNotice result = taskService.create(sc.getUserPrincipal(), id, command);
        return Response.ok(result).build();
    }

    @Path("{id}/taskNotices/{taskNoticeId}")
    @PUT
    public Response update(@Context SecurityContext sc,
                           @Valid @NotBlank @PathParam("id") String id,
                           @Valid @NotBlank @PathParam("taskNoticeId") String taskNoticeId,
                           @Valid @NotNull TaskNoticeUpdateCommand command) throws Exception {
        TaskNotice result = taskService.update(sc.getUserPrincipal(), id, taskNoticeId, command);
        return Response.ok(result).build();
    }

    @Path("{id}/taskNotices")
    @GET
    public Collection<TaskNotice> taskNotices(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return taskNoticeRepository.queryByTaskId(id).collect(Collectors.toList());
    }

    @Path("{id}/taskNotices/{taskNoticeId}")
    @DELETE
    public void deleteTaskNotice(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotBlank @PathParam("taskNoticeId") String taskNoticeId) throws Exception {
        taskService.deleteTaskNotice(sc.getUserPrincipal(), id, taskNoticeId);
    }

    @Path("{id}/taskEvaluates")
    @POST
    public Response create(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull TaskEvaluateUpdateCommand command) throws Exception {
        TaskEvaluate result = taskService.create(sc.getUserPrincipal(), id, command);
        return Response.ok(result).build();
    }

    @Path("{id}/taskEvaluates")
    @GET
    public Collection<TaskEvaluate> taskEvaluates(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        Operator operator = operatorRepository.find(sc.getUserPrincipal());
        return taskEvaluateRepository.queryByTaskId(id)
                .parallel()
                .filter(taskEvaluate -> {
                    if (taskEvaluate.getTask().isManager(operator)) {
                        return true;
                    } else {
                        return Objects.equals(taskEvaluate.getExecutor(), operator);
                    }
                })
                .collect(Collectors.toList());
    }

    @Path("{id}/taskFeedbacks")
    @POST
    public TaskFeedback create(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull TaskFeedbackUpdateCommand command) throws Exception {
        return taskService.create(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}/taskFeedbacks")
    @GET
    public Collection<JsonNode> taskFeedbacks(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        Operator operator = operatorRepository.find(sc.getUserPrincipal());
        return taskFeedbackRepository.queryByTaskId(id)
                .filter(taskFeedback -> {
                    if (taskFeedback.isAtAll() || taskFeedback.getTask().isManager(operator)) {
                        return true;
                    }
                    return Objects.equals(taskFeedback.getCreator(), operator) || J.emptyIfNull(taskFeedback.getAtOperators()).contains(operator);
                })
                .map(taskFeedback -> {
                    ObjectNode node = J.toObjectNode(taskFeedback);
                    ArrayNode taskFeedbackCommentsNode = MAPPER.createArrayNode();
                    node.set("taskFeedbackComments", taskFeedbackCommentsNode);
                    taskFeedbackCommentRepository.queryBy(taskFeedback)
                            .map(MAPPER.getNodeFactory()::pojoNode)
                            .forEach(taskFeedbackCommentsNode::add);
                    return node;
                })
                .collect(Collectors.toList());
    }

    @Path("{id}/taskComplains")
    @GET
    public Collection<TaskComplain> listTaskComplain(@Valid @NotBlank @PathParam("id") String id) {
        return taskComplainRepository.queryByTaskId(id).collect(Collectors.toList());
    }
}

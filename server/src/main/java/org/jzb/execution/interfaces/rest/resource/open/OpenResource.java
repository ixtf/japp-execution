package org.jzb.execution.interfaces.rest.resource.open;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import org.apache.commons.io.FileUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.Util;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.TaskOperatorContextData;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@Path("opens")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class OpenResource {
    @Inject
    private TaskGroupRepository taskGroupRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskOperatorContextDataRepository taskOperatorContextDataRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskEvaluateRepository taskEvaluateRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;

    // 计划打卡
    @Path("shares/{token}/taskClock")
    @GET
    public JsonNode taskClock(@Valid @NotBlank @PathParam("token") String token) throws Exception {
        final Claims claims = Util.jws(token).getBody();
        final ObjectNode result = MAPPER.createObjectNode();
        final String issuer = claims.getIssuer();
        final UserPrincipal principal = new UserPrincipal(issuer);
        final Operator shareOperator = operatorRepository.find(principal);
        result.set("shareOperator", MAPPER.getNodeFactory().pojoNode(shareOperator));
        final ArrayNode taskArray = MAPPER.createArrayNode();
        result.set("tasks", taskArray);
        final String taskId = claims.get("taskId", String.class);
        result.put("taskId", taskId);
        Optional.ofNullable(taskId)
                .map(taskRepository::find)
                .map(Task::getTaskGroup)
                .map(TaskGroup::getId)
                .ifPresent(taskGroupId -> {
                    try {
                        taskRepository.queryByTaskGroupId(principal, taskGroupId)
                                .map(task -> {
                                    ObjectNode taskNode = MAPPER.createObjectNode();
                                    fillTaskData(taskNode, task.getId(), shareOperator);
                                    return taskNode;
                                })
                                .forEach(taskArray::add);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return result;
    }

    @Path("shares/{token}/task")
    @GET
    public JsonNode task(@Valid @NotBlank @PathParam("token") String token) throws Exception {
        final Claims claims = Util.jws(token).getBody();
        final ObjectNode result = MAPPER.createObjectNode();
        final Operator shareOperator = operatorRepository.find(claims.getIssuer());
        result.set("shareOperator", MAPPER.getNodeFactory().pojoNode(shareOperator));
        final String taskId = claims.get("taskId", String.class);
        fillTaskData(result, taskId, shareOperator);
        return result;
    }

    private void fillTaskData(ObjectNode result, String taskId, Operator shareOperator) {
        Task task = taskRepository.find(taskId);
        result.set("task", MAPPER.getNodeFactory().pojoNode(task));
        Collection<TaskOperatorContextData> taskOperatorContextDatas = task.relateOperators()
                .map(it -> taskOperatorContextDataRepository.find(task, it))
                .collect(Collectors.toList());
        result.set("taskOperatorContextDatas", MAPPER.getNodeFactory().pojoNode(taskOperatorContextDatas));
        Collection<JsonNode> taskFeedbacks = taskFeedbackRepository.queryByTaskId(task.getId())
                .filter(taskFeedback -> {
                    if (task.isManager(shareOperator)) {
                        return true;
                    } else {
                        return Objects.equals(taskFeedback.getCreator(), shareOperator);
                    }
                })
                .map(taskFeedback -> {
                    ObjectNode node = MAPPER.convertValue(taskFeedback, ObjectNode.class);
                    Collection<TaskFeedbackComment> taskFeedbackComments = taskFeedbackCommentRepository.queryBy(taskFeedback).collect(Collectors.toList());
                    node.set("taskFeedbackComments", MAPPER.getNodeFactory().pojoNode(taskFeedbackComments));
                    return node;
                })
                .collect(Collectors.toList());
        result.set("taskFeedbacks", MAPPER.getNodeFactory().pojoNode(taskFeedbacks));
        Collection<JsonNode> taskEvaluates = taskEvaluateRepository.queryByTaskId(task.getId())
                .filter(taskEvaluate -> {
                    if (task.isManager(shareOperator)) {
                        return true;
                    } else {
                        return Objects.equals(taskEvaluate.getCreator(), shareOperator);
                    }
                })
                .map(MAPPER.getNodeFactory()::pojoNode)
                .collect(Collectors.toList());
        result.set("taskEvaluates", MAPPER.getNodeFactory().pojoNode(taskEvaluates));
    }

    @Path("shares/{token}/taskGroup")
    @GET
    public JsonNode taskGroup(@Valid @NotBlank @PathParam("token") String token) throws Exception {
        final Claims claims = Util.jws(token).getBody();
        final String taskGroupId = claims.get("taskGroupId", String.class);
        final ObjectNode result = MAPPER.createObjectNode();
        final Operator shareOperator = operatorRepository.find(claims.getIssuer());
        result.set("shareOperator", MAPPER.getNodeFactory().pojoNode(shareOperator));
        final TaskGroup taskGroup = taskGroupRepository.find(taskGroupId);
        result.set("taskGroup", MAPPER.getNodeFactory().pojoNode(taskGroup));
        final ArrayNode taskArray = MAPPER.createArrayNode();
        result.set("tasks", taskArray);
        final Collection<String> taskIds = claims.get("taskIds", Collection.class);
        taskIds.stream()
                .map(taskId -> {
                    ObjectNode taskNode = MAPPER.createObjectNode();
                    fillTaskData(taskNode, taskId, shareOperator);
                    return taskNode;
                })
                .forEach(taskArray::add);
        return result;
    }

    @Path("downloads/{token}")
    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    public Response download(@Context Request request, @Valid @NotBlank @PathParam("token") String token) throws Exception {
        return Util.downloadResponseByDownloadToken(token, request);
    }

    @Path("downloads/{token}/image")
    @GET
    @Produces("image/*")
    public Response image(@Context Request request, @Valid @NotBlank @PathParam("token") String token) throws Exception {
        return Util.commonResponseByDownloadToken(token, request);
    }

    @Path("downloads/{token}/image/w/{width}")
    @GET
    @Produces("image/*")
    public Response image(@Context Request request, @Valid @NotBlank @PathParam("token") String token, @Valid @Min(1) @PathParam("width") int width) throws Exception {
        return Util.imageResponseByDownloadToken(token, request, width);
    }

    @Path("downloads/{token}/video")
    @GET
    @Produces("video/*")
    public Response video(@Context Request request, @Valid @NotBlank @PathParam("token") String token) throws Exception {
        return Util.commonResponseByDownloadToken(token, request);
    }

    @Path("downloads/{token}/html")
    @GET
    public Response html(@Context Request request, @Valid @NotBlank @PathParam("token") String token) throws Exception {
        Claims claims = Util.jws(token).getBody();
        String filePath = claims.get("filePath", String.class);
        ObjectNode result = MAPPER.createObjectNode()
                .put("data", Util.toHtml(FileUtils.getFile(filePath)));
        return Response.ok(result).build();
    }

    @Path("downloads/{token}/pdf")
    @GET
    @Produces("application/pdf")
    public Response pdf(@Context Request request, @Valid @NotBlank @PathParam("token") String token) throws Exception {
        return Util.pdfResponseByDownloadToken(token, request);
    }

}


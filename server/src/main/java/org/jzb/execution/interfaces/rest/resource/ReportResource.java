package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.jboss.resteasy.annotations.GZIP;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskFeedback;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.data.TaskStatus;
import org.jzb.execution.domain.repository.TaskFeedbackCommentRepository;
import org.jzb.execution.domain.repository.TaskFeedbackRepository;
import org.jzb.execution.domain.repository.TaskRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;
import static org.jzb.execution.Constant.MY_TASK_GROUP;

/**
 * Created by jzb on 17-4-15.
 */
@GZIP
@Path("reports")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class ReportResource {
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;

    // @Path("my/manage")
    // @GET
    // public Response myManage(@Context SecurityContext sc) throws Exception {
    //     final ObjectNode result = MAPPER.createObjectNode();
    //     final Principal principal = sc.getUserPrincipal();
    //     final ArrayNode tasksNode = MAPPER.createArrayNode();
    //     result.set("tasks", tasksNode);
    //     final Set<Task> tasks = taskRepository.query(principal)
    //             .filter(it -> it.isManager(principal))
    //             .peek(it->{
    //
    //             })
    //             .collect(Collectors.toSet());
    //     tasks.stream().map(Task::getTaskGroup);
    // }

    @Path("my")
    @POST
    public Response my(@Context SecurityContext sc) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        // taskRepository.query(principal)

        Multimap<TaskGroup, JsonNode> multimap = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

        Function<TaskFeedback, JsonNode> taskFeedbackNodeFun = taskFeedback -> {
            ObjectNode taskFeedbackNode = MAPPER.convertValue(taskFeedback, ObjectNode.class);
            ArrayNode arrayNode = MAPPER.createArrayNode();
            taskFeedbackNode.set("taskFeedbackComments", arrayNode);
            Collection<JsonNode> nodes = taskFeedbackCommentRepository.queryBy(taskFeedback)
                    .map(MAPPER.getNodeFactory()::pojoNode)
                    .collect(Collectors.toList());
            arrayNode.addAll(nodes);
            return taskFeedbackNode;
        };

        BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
        bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
        bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
        Collection<Task> tasks = taskRepository.query(bqBuilder.build()).collect(Collectors.toList());

        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode arrayNode = MAPPER.createArrayNode();
        result.set("taskGroups", arrayNode);
        Collection<JsonNode> nodes = tasks.stream()
                .map(task -> Optional.ofNullable(task.getTaskGroup()).orElse(MY_TASK_GROUP))
                .distinct()
                .map(MAPPER.getNodeFactory()::pojoNode)
                .collect(Collectors.toList());
        arrayNode.addAll(nodes);

        arrayNode = MAPPER.createArrayNode();
        result.set("tasks", arrayNode);
        nodes = tasks.stream()
                .map(task -> {
                    ObjectNode taskNode = MAPPER.convertValue(task, ObjectNode.class);
                    if (task.getTaskGroup() == null) {
                        taskNode.set("taskGroup", MAPPER.getNodeFactory().pojoNode(MY_TASK_GROUP));
                    }
                    ArrayNode taskFeedbacksNode = MAPPER.createArrayNode();
                    taskNode.set("taskFeedbacks", taskFeedbacksNode);
                    Collection<JsonNode> _nodes = taskFeedbackRepository.queryBy(task)
                            .map(taskFeedbackNodeFun)
                            .collect(Collectors.toList());
                    taskFeedbacksNode.addAll(_nodes);
                    return taskNode;
                })
                .collect(Collectors.toList());
        arrayNode.addAll(nodes);
        return Response.ok(result).build();
    }
}


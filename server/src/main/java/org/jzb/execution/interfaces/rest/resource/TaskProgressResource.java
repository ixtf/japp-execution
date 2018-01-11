package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.jzb.J;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.data.TaskStatus;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.repository.TaskFeedbackCommentRepository;
import org.jzb.execution.domain.repository.TaskFeedbackRepository;
import org.jzb.execution.domain.repository.TaskGroupRepository;
import org.jzb.execution.domain.repository.TaskRepository;
import org.jzb.execution.infrastructure.persistence.lucene.TaskLucene;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;
import static org.jzb.execution.Constant.MY_TASK_GROUP;

/**
 * Created by jzb on 17-4-15.
 */
@Path("taskProgress")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskProgressResource {
    @Resource
    private ManagedScheduledExecutorService ses;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;
    @Inject
    private TaskLucene taskLucene;
    @Inject
    private TaskGroupRepository taskGroupRepository;

    @GET
    public Response current(@Context SecurityContext sc,
                            @QueryParam("q") String q,
                            @QueryParam("taskGroupId") String taskGroupId) throws Exception {
        Principal principal = sc.getUserPrincipal();
        Future<Collection<TaskGroup>> taskGroupsFuture = ses.submit(() -> queryTaskGroup(principal, q, false));
        Future<Collection<Task>> tasksFuture = ses.submit(() -> queryTask(principal, q, taskGroupId, false));
        return generate(taskGroupsFuture.get(), tasksFuture.get());
    }

    private Response generate(Collection<TaskGroup> taskGroups, Collection<Task> tasks) {
        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode taskGroupsNode = MAPPER.createArrayNode();
        result.set("taskGroups", taskGroupsNode);
        ArrayNode tasksNode = MAPPER.createArrayNode();
        result.set("tasks", tasksNode);
        taskGroups.stream().map(MAPPER.getNodeFactory()::pojoNode).forEach(taskGroupsNode::add);
        tasks.stream().map(MAPPER.getNodeFactory()::pojoNode).forEach(tasksNode::add);
        return Response.ok(result).build();
    }

    private Collection<TaskGroup> queryTaskGroup(Principal principal, String q, boolean isHistory) throws IOException, ParseException {
        try (Analyzer analyzer = new SmartChineseAnalyzer(); IndexReader indexReader = taskLucene.indexReader(); DirectoryTaxonomyReader taxoReader = taskLucene.taxoReader()) {
            BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
            bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
            if (isHistory) {
                bqBuilder.add(new TermQuery(new Term("status", TaskStatus.FINISH.name())), BooleanClause.Occur.MUST);
            } else {
                bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
            }
            if (StringUtils.isNotBlank(q)) {
                QueryParser parser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
                bqBuilder.add(parser.parse(q), BooleanClause.Occur.MUST);
            }

            IndexSearcher searcher = new IndexSearcher(indexReader);
            FacetsCollector fc = new FacetsCollector();
            searcher.search(bqBuilder.build(), fc);
            Facets facets = new FastTaxonomyFacetCounts("taskGroup", taxoReader, taskLucene.facetsConfig(), fc);
            FacetResult fr = facets.getTopChildren(Integer.MAX_VALUE, "taskGroup");
            if (fr == null) {
                return Collections.emptyList();
            }
            return Arrays.stream(fr.labelValues)
                    .map(l -> l.label)
                    .filter(id -> !"0".equals(id))
                    .map(taskGroupRepository::find)
                    .collect(Collectors.toList());
        }
    }

    private Collection<Task> queryTask(Principal principal, String q, String taskGroupId, boolean isHistory) throws IOException, ParseException {
        try (Analyzer analyzer = new SmartChineseAnalyzer(); IndexReader indexReader = taskLucene.indexReader()) {
            BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
            bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
            if (isHistory) {
                bqBuilder.add(new TermQuery(new Term("status", TaskStatus.FINISH.name())), BooleanClause.Occur.MUST);
            } else {
                bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
            }
            if (StringUtils.isNotBlank(q)) {
                QueryParser parser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
                bqBuilder.add(parser.parse(q), BooleanClause.Occur.MUST);
            }
            if (StringUtils.isNotBlank(taskGroupId)) {
                bqBuilder.add(new TermQuery(new Term("taskGroup", taskGroupId)), BooleanClause.Occur.MUST);
            }

            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(bqBuilder.build(), Integer.MAX_VALUE);
            if (topDocs.totalHits < 1) {
                return Collections.emptyList();
            }
            Collection<Task> tasks = Lists.newArrayList();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                Optional.ofNullable(taskRepository.find(doc.get("id"))).ifPresent(tasks::add);
            }
            return tasks;
        }
    }

    @Path("history")
    @GET
    public Response history(@Context SecurityContext sc,
                            @QueryParam("q") String q,
                            @QueryParam("taskGroupId") String taskGroupId) throws Exception {
        Principal principal = sc.getUserPrincipal();
        Future<Collection<TaskGroup>> taskGroupsFuture = ses.submit(() -> queryTaskGroup(principal, q, true));
        Future<Collection<Task>> tasksFuture = ses.submit(() -> queryTask(principal, q, taskGroupId, true));
        return generate(taskGroupsFuture.get(), tasksFuture.get());
    }

    @Path("examQuestionReview")
    @GET
    public Response examQuestionReview(@Context SecurityContext sc,
                                       @QueryParam("q") String q,
                                       @QueryParam("taskGroupId") String taskGroupId) throws Exception {
        Multimap<TaskGroup, JsonNode> multimap = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

        Principal principal = sc.getUserPrincipal();
        BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
        bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
        bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
        taskRepository.query(bqBuilder.build())
                .forEach(task -> {
                    Collection<ExamQuestion> examQuestions = taskFeedbackRepository.queryBy(task)
                            .filter(taskFeedback -> taskFeedback.getCreator().getId().equals(principal.getName()))
                            .flatMap(taskFeedbackCommentRepository::queryBy)
                            .map(TaskFeedbackComment::getExamQuestions)
                            .filter(J::nonEmpty)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toSet());
                    if (J.nonEmpty(examQuestions)) {
                        ObjectNode taskNode = MAPPER.convertValue(task, ObjectNode.class);
                        ArrayNode examQuestionsNode = MAPPER.createArrayNode();
                        examQuestions.stream().map(MAPPER.getNodeFactory()::pojoNode).forEach(examQuestionsNode::add);
                        taskNode.set("examQuestions", examQuestionsNode);
                        TaskGroup taskGroup = Optional.ofNullable(task.getTaskGroup()).orElse(MY_TASK_GROUP);
                        multimap.put(taskGroup, taskNode);
                    }
                });

        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode taskGroups = MAPPER.createArrayNode();
        result.set("taskGroups", taskGroups);
        multimap.keySet()
                .stream()
                .map(MAPPER.getNodeFactory()::pojoNode)
                .forEach(taskGroups::add);
        ArrayNode tasks = MAPPER.createArrayNode();
        result.set("tasks", tasks);
        TaskGroup taskGroup = multimap.keySet()
                .stream()
                .filter(it -> Objects.equals(it.getId(), taskGroupId))
                .findFirst()
                .orElseGet(() -> multimap.keySet().stream().findFirst().orElse(null));
        Optional.ofNullable(taskGroup)
                .map(multimap::get)
                .filter(Objects::nonNull)
                .ifPresent(it -> it.stream().forEach(tasks::add));
        return Response.ok(result).build();
    }

    @Path("examQuestionReviewManager")
    @GET
    public Response examQuestionReviewManager(@Context SecurityContext sc,
                                              @QueryParam("q") String q,
                                              @QueryParam("taskGroupId") String taskGroupId) throws Exception {
        Principal principal = sc.getUserPrincipal();
        BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
        bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
        bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
        Map<TaskGroup, List<Task>> map = taskRepository.query(bqBuilder.build())
                .filter(task -> task.isManager(principal))
                .filter(task -> taskFeedbackRepository.queryBy(task)
                        .flatMap(taskFeedbackCommentRepository::queryBy)
                        .map(TaskFeedbackComment::getExamQuestions)
                        .filter(J::nonEmpty)
                        .findFirst()
                        .map((it) -> true)
                        .orElse(false))
                .collect(Collectors.groupingBy(task -> Optional.ofNullable(task.getTaskGroup()).orElse(MY_TASK_GROUP)));

        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode taskGroups = MAPPER.createArrayNode();
        result.set("taskGroups", taskGroups);
        map.keySet()
                .stream()
                .map(MAPPER.getNodeFactory()::pojoNode)
                .forEach(taskGroups::add);
        ArrayNode tasks = MAPPER.createArrayNode();
        result.set("tasks", tasks);
        TaskGroup taskGroup = map.keySet()
                .stream()
                .filter(it -> Objects.equals(it.getId(), taskGroupId))
                .findFirst()
                .orElseGet(() -> map.keySet().stream().findFirst().orElse(null));
        Optional.ofNullable(taskGroup)
                .map(map::get)
                .filter(Objects::nonNull)
                .ifPresent(it -> it.stream().map(MAPPER.getNodeFactory()::pojoNode).forEach(tasks::add));
        return Response.ok(result).build();
    }

}

package org.jzb.execution.interfaces.rest.resource.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.TaskComplain;
import org.jzb.execution.domain.data.TaskStatus;
import org.jzb.execution.domain.repository.*;
import org.jzb.execution.infrastructure.persistence.lucene.TaskLucene;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class AdminTaskResource {
    @Resource
    private ManagedScheduledExecutorService ses;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskEvaluateRepository taskEvaluateRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;
    @Inject
    private TaskLucene taskLucene;

    @GET
    public JsonNode current(@QueryParam("history") boolean history,
                            @QueryParam("q") String q,
                            @Valid @NotNull @Min(10) @Max(500) @DefaultValue("10") @QueryParam("pageSize") int pageSize,
                            @Valid @NotNull @Min(0) @QueryParam("first") int first) throws Exception {
        final ObjectNode result = MAPPER.createObjectNode()
                .put("first", first)
                .put("pageSize", pageSize);
        final ArrayNode tasks = MAPPER.createArrayNode();
        result.set("tasks", tasks);
        try (Analyzer analyzer = new SmartChineseAnalyzer(); IndexReader indexReader = taskLucene.indexReader()) {
            BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
            if (history) {
                bqBuilder.add(new TermQuery(new Term("status", TaskStatus.FINISH.name())), BooleanClause.Occur.MUST);
            } else {
                bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
            }
            if (StringUtils.isNotBlank(q)) {
                QueryParser parser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
                bqBuilder.add(parser.parse(q), BooleanClause.Occur.MUST);
            }

            IndexSearcher searcher = new IndexSearcher(indexReader);
            final SortedNumericSortField sortField = new SortedNumericSortField("modify_date", SortField.Type.LONG, true);
            TopDocs topDocs = searcher.search(bqBuilder.build(), first + pageSize, new Sort(sortField));
            result.put("count", topDocs.totalHits);
            if (topDocs.totalHits < 1) {
                return result;
            }
            Arrays.stream(topDocs.scoreDocs)
                    .skip(first)
                    .limit(pageSize)
                    .map(it -> toTaskNode(searcher, it))
                    .filter(Objects::nonNull)
                    .forEach(tasks::add);
            return result;
        }
    }

    private JsonNode toTaskNode(IndexSearcher searcher, ScoreDoc scoreDoc) {
        try {
            Document doc = searcher.doc(scoreDoc.doc);
            return Optional.ofNullable(taskRepository.find(doc.get("id")))
                    .map(task -> {
                        final ObjectNode taskNode = MAPPER.convertValue(task, ObjectNode.class);
                        final ArrayNode taskEvaluatesNode = MAPPER.createArrayNode();
                        taskNode.set("taskEvaluates", taskEvaluatesNode);
                        taskEvaluateRepository.queryByTaskId(task.getId())
                                .map(taskEvaluate -> {
                                    final ObjectNode taskEvaluateNode = MAPPER.convertValue(taskEvaluate, ObjectNode.class);
                                    return taskEvaluateNode;
                                })
                                .forEach(taskEvaluatesNode::add);
                        final ArrayNode taskFeedbacksNode = MAPPER.createArrayNode();
                        taskNode.set("taskFeedbacks", taskFeedbacksNode);
                        taskFeedbackRepository.queryBy(task)
                                .map(taskFeedback -> {
                                    final ObjectNode taskFeedbackNode = MAPPER.convertValue(taskFeedback, ObjectNode.class);
                                    final ArrayNode taskFeedbackCommentsNode = MAPPER.createArrayNode();
                                    taskFeedbackCommentRepository.queryBy(taskFeedback)
                                            .map(taskFeedbackComment -> {
                                                final ObjectNode taskFeedbackCommentNode = MAPPER.convertValue(taskFeedbackComment, ObjectNode.class);
                                                return taskFeedbackCommentNode;
                                            })
                                            .forEach(taskFeedbackCommentsNode::add);
                                    return taskFeedbackNode;
                                })
                                .forEach(taskFeedbacksNode::add);
                        return taskNode;
                    })
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

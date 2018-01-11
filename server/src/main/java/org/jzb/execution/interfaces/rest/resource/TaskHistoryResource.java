package org.jzb.execution.interfaces.rest.resource;

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
import org.jboss.resteasy.annotations.GZIP;
import org.jzb.execution.domain.data.TaskStatus;
import org.jzb.execution.domain.repository.TaskRepository;
import org.jzb.execution.infrastructure.persistence.lucene.TaskLucene;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@GZIP
@Path("taskHistory")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskHistoryResource {
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskLucene taskLucene;

    @GET
    public Response current(@Context SecurityContext sc,
                            @QueryParam("q") String q,
                            @Valid @Min(0) @DefaultValue("0") @QueryParam("first") int first,
                            @Valid @Min(1) @Max(1000) @DefaultValue("50") @QueryParam("pageSize") int pageSize) throws Exception {
        ObjectNode result = MAPPER.createObjectNode();
        ArrayNode tasksNode = MAPPER.createArrayNode();
        result.set("tasks", tasksNode);

        Principal principal = sc.getUserPrincipal();
        try (Analyzer analyzer = new SmartChineseAnalyzer(); IndexReader indexReader = taskLucene.indexReader()) {
            Sort sort = new Sort();
            sort.setSort(new SortField("modify_date", SortField.Type.LONG, true));

            BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
            bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
            bqBuilder.add(new TermQuery(new Term("status", TaskStatus.FINISH.name())), BooleanClause.Occur.MUST);

            if (StringUtils.isNotBlank(q)) {
                QueryParser parser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
                bqBuilder.add(parser.parse(q), BooleanClause.Occur.MUST);
            }

            IndexSearcher searcher = new IndexSearcher(indexReader);
            Query query = bqBuilder.build();
            ScoreDoc after = getAfterScoreDoc(first, searcher, query, sort);
            TopDocs topDocs = searcher.searchAfter(after, query, pageSize, sort);
            result.put("count", topDocs.totalHits);
            if (topDocs.totalHits > 0) {
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    Document doc = searcher.doc(scoreDoc.doc);
                    Optional.ofNullable(taskRepository.find(doc.get("id")))
                            .map(MAPPER.getNodeFactory()::pojoNode)
                            .ifPresent(tasksNode::add);
                }
            }
        }
        return Response.ok(result).build();
    }

    private ScoreDoc getAfterScoreDoc(int first, IndexSearcher searcher, Query query, Sort sort) throws IOException {
        if (first == 0) {
            return null;
        }
        TopDocs topDocs = searcher.search(query, first, sort);
        if (topDocs.scoreDocs.length < 1) {
            return null;
        }
        return topDocs.scoreDocs[topDocs.scoreDocs.length - 1];
    }
}

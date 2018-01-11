/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.infrastructure.persistence.lucene;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.jzb.J;
import org.jzb.execution.application.query.TaskQuery;
import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.data.TaskStatus;
import org.jzb.execution.domain.repository.TaskRepository;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.jzb.Constant.MAPPER;

/**
 * @author jzb
 */
@ApplicationScoped
public class TaskLucene extends AbstractBaseLucene<Task> {
    @Inject
    private Logger log;
    @Inject
    private TaskRepository taskRepository;

    @Override
    protected FacetsConfig _facetsConfig() {
        FacetsConfig result = new FacetsConfig();
        result.setIndexFieldName("channel", "channel");
        result.setIndexFieldName("plan", "plan");
        result.setIndexFieldName("taskGroup", "taskGroup");
        result.setMultiValued("tags", true);
        result.setIndexFieldName("tags", "tags");
        result.setMultiValued("relate_operator", true);
        result.setIndexFieldName("relate_operator", "relate_operator");
        return result;
    }

    @Override
    public Document document(Task o) {
        Document doc = new Document();
        doc.add(new StringField("id", o.getId(), Field.Store.YES));

        String channelId = Optional.ofNullable(o.getPlan())
                .map(Plan::getChannel)
                .map(Channel::getId)
                .orElse("0");
        doc.add(new StringField("channel", channelId, Field.Store.YES));
        doc.add(new FacetField("channel", channelId));

        String planId = Optional.ofNullable(o.getPlan())
                .map(Plan::getId)
                .orElse("0");
        doc.add(new StringField("plan", planId, Field.Store.YES));
        doc.add(new FacetField("plan", planId));

        String taskGroupId = Optional.ofNullable(o.getTaskGroup())
                .map(TaskGroup::getId)
                .orElse("0");
        doc.add(new StringField("taskGroup", taskGroupId, Field.Store.YES));
        doc.add(new FacetField("taskGroup", taskGroupId));

        doc.add(new TextField("title", o.getTitle(), Field.Store.NO));
        if (!isBlank(o.getContent())) {
            doc.add(new TextField("content", o.getContent(), Field.Store.NO));
        }
        doc.add(new LongPoint("start_date", o.getStartDate().getTime()));
        doc.add(new NumericDocValuesField("start_date", o.getStartDate().getTime()));
        doc.add(new LongPoint("end_date", o.getEndDate().getTime()));
        doc.add(new NumericDocValuesField("end_date", o.getEndDate().getTime()));
        doc.add(new StringField("status", o.getStatus().name(), Field.Store.NO));
        doc.add(new StringField("charger", o.getCharger().getId(), Field.Store.YES));

        doc.add(new LongPoint("create_date", o.getCreateDateTime().getTime()));
        doc.add(new NumericDocValuesField("create_date", o.getCreateDateTime().getTime()));
        doc.add(new LongPoint("modify_date", o.getModifyDateTime().getTime()));
        doc.add(new NumericDocValuesField("modify_date", o.getModifyDateTime().getTime()));

        addRelateOperatorField(doc, o.relateOperators());
        addTags(doc, o.getTags());
        return doc;
    }

    public Stream<Task> query(Query query) throws IOException {
        try (IndexReader indexReader = indexReader()) {
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);
            if (topDocs.totalHits < 1) {
                return Stream.empty();
            }
            return Arrays.stream(topDocs.scoreDocs)
                    .parallel()
                    .map(scoreDoc -> toDocument(searcher, scoreDoc))
                    .map(it -> it.get("id"))
                    .filter(J::nonBlank)
                    .collect(Collectors.toSet())
                    .stream()
                    .map(taskRepository::find)
                    .filter(Objects::nonNull);
        }
    }

    public Stream<Task> query(Principal principal) throws IOException {
        BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
        bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
        bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
        return query(bqBuilder.build());
    }

    public Stream<Task> queryByTaskGroupId(Principal principal, String taskGroupId) throws IOException {
        BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
        bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
        bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
        bqBuilder.add(new TermQuery(new Term("taskGroup", taskGroupId)), BooleanClause.Occur.MUST);
        return query(bqBuilder.build());
    }

    public void query(TaskQuery command) throws Exception {
        try (Analyzer analyzer = new SmartChineseAnalyzer(); IndexReader indexReader = indexReader()) {
            BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
            bqBuilder.add(new TermQuery(new Term("relate_operator", command.principal.getName())), BooleanClause.Occur.MUST);
            if (command.isHistory()) {
                bqBuilder.add(new TermQuery(new Term("status", TaskStatus.FINISH.name())), BooleanClause.Occur.MUST);
            } else {
                bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);
            }
            if (StringUtils.isNotBlank(command.q())) {
                QueryParser parser = new MultiFieldQueryParser(new String[]{"title", "content"}, analyzer);
                bqBuilder.add(parser.parse(command.q()), BooleanClause.Occur.MUST);
            }

            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(bqBuilder.build(), Integer.MAX_VALUE);
            if (topDocs.totalHits < 1) {
                command.result = MAPPER.createArrayNode();
                return;
            }
            ArrayNode tasks = MAPPER.createArrayNode();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                Optional.ofNullable(taskRepository.find(doc.get("id")))
                        .map(MAPPER.getNodeFactory()::pojoNode)
                        .ifPresent(tasks::add);
            }
            command.result = tasks;
        }
    }
}

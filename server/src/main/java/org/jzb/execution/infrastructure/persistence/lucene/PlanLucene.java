/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.infrastructure.persistence.lucene;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.jzb.execution.application.query.PlanQuery;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.repository.PlanRepository;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.security.Principal;
import java.util.Optional;

import static org.jzb.Constant.MAPPER;

/**
 * @author jzb
 */
@ApplicationScoped
public class PlanLucene extends AbstractBaseLucene<Plan> {
    @Inject
    protected EntityManager em;
    @Inject
    private Logger log;
    @Inject
    private PlanRepository planRepository;

    // @PostConstruct
    // void test() {
    //     try {
    //         for (Plan plan : em.createNamedQuery("Plan.find", Plan.class).getResultList()) {
    //             Term term = new Term("id", plan.getId());
    //             indexWriter.updateDocument(term, facetsConfig.build(taxoWriter, document(plan)));
    //         }
    //         indexWriter.commit();
    //         taxoWriter.commit();
    //     } catch (Exception ex) {
    //         log.error("", ex);
    //         throw new RuntimeException(ex);
    //     }
    // }

    @Override
    protected FacetsConfig _facetsConfig() {
        FacetsConfig result = new FacetsConfig();
        result.setIndexFieldName("channel", "channel");
        result.setMultiValued("tags", true);
        result.setIndexFieldName("tags", "tags");
        return result;
    }

    @Override
    public Document document(Plan o) {
        Document doc = new Document();
        doc.add(new StringField("id", o.getId(), Field.Store.YES));
        doc.add(new StringField("channel", o.getChannel().getId(), Field.Store.NO));
        doc.add(new FacetField("channel", o.getChannel().getId()));
        doc.add(new TextField("name", o.getName(), Field.Store.NO));
        Optional.ofNullable(o.getNote())
                .ifPresent(note -> doc.add(new TextField("note", o.getNote(), Field.Store.NO)));
        doc.add(new IntPoint("shared", o.isShared() ? 1 : 0));
        doc.add(new IntPoint("published", o.isPublished() ? 1 : 0));
        doc.add(new IntPoint("audited", o.isAudited() ? 1 : 0));
        addTags(doc, o.getTags());
        doc.add(new StringField("creator", o.getCreator().getId(), Field.Store.YES));
        doc.add(new LongPoint("create_date_time", o.getCreateDateTime().getTime()));
        doc.add(new NumericDocValuesField("create_date_time", o.getCreateDateTime().getTime()));
        doc.add(new StringField("modifier", o.getModifier().getId(), Field.Store.YES));
        doc.add(new LongPoint("modify_date_time", o.getModifyDateTime().getTime()));
        doc.add(new NumericDocValuesField("modify_date_time", o.getModifyDateTime().getTime()));
        return doc;
    }

    public void query(PlanQuery command) throws Exception {
        ObjectNode result = MAPPER.createObjectNode()
                .put("first", command.first)
                .put("pageSize", command.pageSize);
        command.result = result;
        try (Analyzer analyzer = new SmartChineseAnalyzer(); IndexReader indexReader = indexReader()) {
            BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
            Optional.ofNullable(command.principal)
                    .map(Principal::getName)
                    .map(it -> new Term("creator", it))
                    .map(TermQuery::new)
                    .ifPresent(it -> bqBuilder.add(it, BooleanClause.Occur.MUST));
            Optional.ofNullable(command.getChannelId())
                    .map(it -> new Term("channel", it))
                    .map(TermQuery::new)
                    .ifPresent(it -> bqBuilder.add(it, BooleanClause.Occur.MUST));
            Optional.ofNullable(command.getAudited())
                    .map(it -> it ? 1 : 0)
                    .map(it -> IntPoint.newExactQuery("audited", it))
                    .ifPresent(it -> bqBuilder.add(it, BooleanClause.Occur.MUST));
            Optional.ofNullable(command.getPublished())
                    .map(it -> it ? 1 : 0)
                    .map(it -> IntPoint.newExactQuery("published", it))
                    .ifPresent(it -> bqBuilder.add(it, BooleanClause.Occur.MUST));
            Optional.ofNullable(command.getShared())
                    .map(it -> it ? 1 : 0)
                    .map(it -> IntPoint.newExactQuery("shared", it))
                    .ifPresent(it -> bqBuilder.add(it, BooleanClause.Occur.MUST));

            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(bqBuilder.build(), Integer.MAX_VALUE);

            ArrayNode plans = MAPPER.createArrayNode();
            if (topDocs.totalHits < 1) {
                result.put("count", 0);
                result.set("plans", plans);
                return;
            }
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                Optional.ofNullable(planRepository.find(doc.get("id")))
                        .map(MAPPER.getNodeFactory()::pojoNode)
                        .ifPresent(plans::add);
            }
            result.put("count", topDocs.totalHits);
            result.set("plans", plans);
        }
    }
}

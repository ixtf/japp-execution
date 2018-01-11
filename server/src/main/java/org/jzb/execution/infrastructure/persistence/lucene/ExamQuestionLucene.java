/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.infrastructure.persistence.lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetsConfig;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.repository.ExamQuestionRepository;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * @author jzb
 */
@ApplicationScoped
public class ExamQuestionLucene extends AbstractBaseLucene<ExamQuestion> {
    @Inject
    private Logger log;
    @Inject
    private ExamQuestionRepository examQuestionRepository;

    @PostConstruct
    void test() {
//        try (Analyzer analyzer = new SmartChineseAnalyzer();
//             Directory indexDir = FSDirectory.open(INDEX_PATH); IndexWriter indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(analyzer));
//             Directory taxoDir = FSDirectory.open(TAXO_PATH); DirectoryTaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter(taxoDir)) {
//            for (Experience experience : em.createNamedQuery("Experience.find", Experience.class).getResultList()) {
//                indexWriter.updateDocument(new Term("id", experience.getId()), FACETS_CONFIG.build(taxoWriter, getDocument(experience)));
//            }
//            indexWriter.commit();
//            taxoWriter.commit();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    @Override
    protected FacetsConfig _facetsConfig() {
        FacetsConfig result = new FacetsConfig();
        result.setIndexFieldName("lab", "lab");
        result.setMultiValued("tags", true);
        result.setIndexFieldName("tags", "tags");
        return result;
    }

    @Override
    public Document document(ExamQuestion o) {
        Document doc = new Document();
        doc.add(new StringField("id", o.getId(), Field.Store.YES));
        String labId = Optional.ofNullable(o.getLab())
                .map(ExamQuestionLab::getId)
                .orElse("0");
        doc.add(new StringField("lab", labId, Field.Store.YES));
        doc.add(new TextField("name", o.getName(), Field.Store.NO));

        addTags(doc, o.getTags());

        doc.add(new StringField("creator", o.getCreator().getId(), Field.Store.YES));
        doc.add(new LongPoint("create_date", o.getCreateDateTime().getTime()));
        doc.add(new NumericDocValuesField("create_date", o.getCreateDateTime().getTime()));
        doc.add(new StringField("modifier", o.getModifier().getId(), Field.Store.YES));
        doc.add(new LongPoint("modify_date", o.getModifyDateTime().getTime()));
        doc.add(new NumericDocValuesField("modify_date", o.getModifyDateTime().getTime()));
        return doc;
    }
}

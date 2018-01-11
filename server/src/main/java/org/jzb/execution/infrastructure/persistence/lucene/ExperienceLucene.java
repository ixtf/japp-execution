/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.infrastructure.persistence.lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetsConfig;
import org.jzb.execution.domain.Experience;
import org.jzb.execution.domain.repository.ExperienceRepository;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author jzb
 */
@ApplicationScoped
public class ExperienceLucene extends AbstractBaseLucene<Experience> {

    static {
    }

    @Inject
    private Logger log;
    @Inject
    private ExperienceRepository experienceRepository;

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
        result.setMultiValued("tags", true);
        result.setIndexFieldName("tags", "tags");
        return result;
    }

    @Override
    public Document document(Experience o) {
        Document doc = new Document();
        doc.add(new StringField("id", o.getId(), Field.Store.YES));
        doc.add(new IntPoint("share", o.isShare() ? 1 : 0));
        doc.add(new TextField("name", o.getName(), Field.Store.NO));
        doc.add(new TextField("content", o.getContent(), Field.Store.NO));
        addTags(doc, o.getTags());
        addAttachments(doc, o.getAttachments());
        return doc;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.infrastructure.persistence.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.FacetsConfig;
import org.jzb.J;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;

/**
 * @author jzb
 */
@ApplicationScoped
public class OperatorLucene extends AbstractBaseLucene<Operator> {
    private static Pattern zwP = Pattern.compile("([\u4e00-\u9fa5]+)");
    @Inject
    private Logger log;
    @Inject
    private OperatorRepository operatorRepository;

    @PostConstruct
    public void test() {
//        try (Analyzer analyzer = new StandardAnalyzer();
//             Directory indexDir = FSDirectory.open(INDEX_PATH); IndexWriter indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(analyzer));
//             Directory taxoDir = FSDirectory.open(TAXO_PATH); DirectoryTaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter(taxoDir)) {
//            for (Operator operator : em.createNamedQuery("Operator.find", Operator.class).getResultList()) {
//                if (!operator.isDeleted()) {
//                    indexWriter.updateDocument(new Term("id", operator.getId()), FACETS_CONFIG.build(taxoWriter, getDocument(operator)));
//                }
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
    public Document document(Operator o) {
        Document doc = new Document();
        doc.add(new StringField("id", o.getId(), Field.Store.YES));
        doc.add(new StringField("name", o.getName(), Field.Store.NO));
        doc.add(new StringField("search", o.getName(), Field.Store.NO));

        Matcher matcher = zwP.matcher(deleteWhitespace(o.getName()));
        while (matcher.find()) {
            J.pyAbbr(matcher.group(0)).forEach(py -> doc.add(new TextField("search", py, Field.Store.NO)));
        }
        return doc;
    }
}

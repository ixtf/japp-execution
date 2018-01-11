package org.jzb.execution.infrastructure.persistence.lucene;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.SearcherTaxonomyManager;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.jzb.J;
import org.jzb.execution.domain.UploadFile;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.share.CURDEntity;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import static org.jzb.execution.Constant.LUCENE_BASE_PATH;


/**
 * Created by jzb on 16-11-24.
 */
public abstract class AbstractBaseLucene<T extends CURDEntity<String>> {
    protected FacetsConfig facetsConfig;
    protected IndexWriter indexWriter;
    protected DirectoryTaxonomyWriter taxoWriter;
    protected SearcherTaxonomyManager searcherTaxonomyManager;
    @Inject
    protected Logger log;

    @PostConstruct
    void _PostConstruct() {
        try {
            facetsConfig = _facetsConfig();

            String indexPath = getIndexPath();
            FSDirectory indexDirectory = FSDirectory.open(Paths.get(LUCENE_BASE_PATH, indexPath));
            indexWriter = new IndexWriter(indexDirectory, new IndexWriterConfig(new SmartChineseAnalyzer()));

            String taxoPath = indexPath + "_Taxonomy";
            FSDirectory taxoDirectory = FSDirectory.open(Paths.get(LUCENE_BASE_PATH, taxoPath));
            taxoWriter = new DirectoryTaxonomyWriter(taxoDirectory);

//            searcherTaxonomyManager = new SearcherTaxonomyManager(indexWriter, true, null, taxoWriter);
        } catch (Throwable e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    protected abstract FacetsConfig _facetsConfig();

    protected String getIndexPath() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        return clazz.getSimpleName();
    }

    @PreDestroy
    void _PreDestroy() {
        try {
            taxoWriter.close();
            indexWriter.close();
        } catch (Throwable e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    public FacetsConfig facetsConfig() {
        return facetsConfig;
    }

    public IndexReader indexReader() throws IOException {
        return DirectoryReader.open(indexWriter);
    }

    public DirectoryTaxonomyReader taxoReader() throws IOException {
        return new DirectoryTaxonomyReader(taxoWriter);
    }

    public void index(T t) {
        if (t.isDeleted()) {
            delete(t.getId());
            return;
        }
        try {
            Term term = new Term("id", t.getId().toString());
            indexWriter.updateDocument(term, facetsConfig.build(taxoWriter, document(t)));
            indexWriter.commit();
            taxoWriter.commit();
        } catch (Throwable e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {
        try {
            Term term = new Term("id", id);
            indexWriter.deleteDocuments(term);
            indexWriter.commit();
            taxoWriter.commit();
        } catch (Throwable e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    protected abstract Document document(T o);

    protected Document toDocument(IndexSearcher searcher, ScoreDoc scoreDoc) {
        try {
            return searcher.doc(scoreDoc.doc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addRelateOperatorField(Document doc, Stream<Operator> stream) {
        stream.distinct()
                .filter(Objects::nonNull)
                .forEach(operator -> {
                    doc.add(new StringField("relate_operator", operator.getId(), Field.Store.NO));
                });
    }

    protected void addTags(Document doc, Collection<String> tags) {
        J.emptyIfNull(tags).stream().distinct()
                .forEach(tag -> {
                    doc.add(new StringField("tags", tag, Field.Store.YES));
                    doc.add(new FacetField("tags", tag));
                });
    }

    protected void addAttachments(Document doc, Collection<UploadFile> uploadFiles) {
        J.emptyIfNull(uploadFiles).stream().distinct()
                .forEach(uploadFile -> {
                    doc.add(new StringField("attachments", uploadFile.getId(), Field.Store.YES));
                });
    }
}

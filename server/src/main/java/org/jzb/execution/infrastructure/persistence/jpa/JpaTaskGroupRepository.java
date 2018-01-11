package org.jzb.execution.infrastructure.persistence.jpa;

import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.data.TaskStatus;
import org.jzb.execution.domain.repository.TaskGroupRepository;
import org.jzb.execution.infrastructure.persistence.lucene.TaskLucene;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaTaskGroupRepository extends JpaCURDRepository<TaskGroup, String> implements TaskGroupRepository {
    @Inject
    private TaskLucene taskLucene;

    @Override
    public Stream<TaskGroup> query(Principal principal) throws Exception {
        try (IndexReader indexReader = taskLucene.indexReader(); DirectoryTaxonomyReader taxoReader = taskLucene.taxoReader()) {
            BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
            bqBuilder.add(new TermQuery(new Term("relate_operator", principal.getName())), BooleanClause.Occur.MUST);
            bqBuilder.add(new TermQuery(new Term("status", TaskStatus.RUN.name())), BooleanClause.Occur.MUST);

            IndexSearcher searcher = new IndexSearcher(indexReader);
            FacetsCollector fc = new FacetsCollector();
            searcher.search(bqBuilder.build(), fc);
            Facets facets = new FastTaxonomyFacetCounts("taskGroup", taxoReader, taskLucene.facetsConfig(), fc);
            FacetResult fr = facets.getTopChildren(Integer.MAX_VALUE, "taskGroup");
            if (fr == null) {
                return Stream.empty();
            }
            return Arrays.stream(fr.labelValues)
                    .map(l -> l.label)
                    .filter(id -> !"0".equals(id))
                    .map(this::find);
        }
    }
}

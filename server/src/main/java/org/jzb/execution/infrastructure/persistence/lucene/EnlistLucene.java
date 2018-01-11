/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.infrastructure.persistence.lucene;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.jzb.execution.domain.Enlist;
import org.jzb.execution.domain.EnlistFeedback;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.EnlistFeedbackRepository;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author jzb
 */
@ApplicationScoped
public class EnlistLucene extends AbstractBaseLucene<Enlist> {

    @Inject
    private Logger log;
    @Inject
    private EnlistFeedbackRepository enlistFeedbackRepository;

    @Override
    protected FacetsConfig _facetsConfig() {
        FacetsConfig result = new FacetsConfig();
        result.setMultiValued("tags", true);
        result.setIndexFieldName("tags", "tags");
        result.setMultiValued("relate_operator", true);
        result.setIndexFieldName("relate_operator", "relate_operator");
        return result;
    }

    @Override
    public Document document(Enlist o) {
        Document doc = new Document();
        doc.add(new StringField("id", o.getId(), Field.Store.YES));

        doc.add(new TextField("title", o.getTitle(), Field.Store.NO));
        if (!isBlank(o.getContent())) {
            doc.add(new TextField("content", o.getContent(), Field.Store.NO));
        }
        doc.add(new LongPoint("start_date", o.getStartDate().getTime()));
        doc.add(new NumericDocValuesField("start_date", o.getStartDate().getTime()));
        doc.add(new LongPoint("end_date", o.getEndDate().getTime()));
        doc.add(new NumericDocValuesField("end_date", o.getEndDate().getTime()));
        doc.add(new StringField("status", o.getStatus().name(), Field.Store.NO));

        doc.add(new LongPoint("create_date", o.getCreateDateTime().getTime()));
        doc.add(new NumericDocValuesField("create_date", o.getCreateDateTime().getTime()));
        doc.add(new LongPoint("modify_date", o.getModifyDateTime().getTime()));
        doc.add(new NumericDocValuesField("modify_date", o.getModifyDateTime().getTime()));

        CollectionUtils.emptyIfNull(o.getTags())
                .parallelStream()
                .flatMap(it -> {
                    final StringField stringField = new StringField("tags", it, Field.Store.YES);
                    final FacetField facetField = new FacetField("tags", it);
                    return Stream.of(stringField, facetField);
                })
                .forEach(doc::add);

        Stream<Operator> operatorStream1 = enlistFeedbackRepository.queryBy(o)
                .map(EnlistFeedback::getCreator);
        Stream<Operator> operatorStream2 = CollectionUtils.emptyIfNull(o.getPaymentMerchant().getManagers()).stream();
        Stream.concat(operatorStream1, operatorStream2)
                .parallel()
                .distinct()
                .flatMap(it -> {
                    final String itId = it.getId();
                    final StringField stringField = new StringField("relate_operator", itId, Field.Store.NO);
                    final FacetField facetField = new FacetField("relate_operator", itId);
                    return Stream.of(stringField, facetField);
                })
                .forEach(doc::add);
        return doc;
    }

}

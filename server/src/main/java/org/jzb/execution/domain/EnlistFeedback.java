/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.collections4.CollectionUtils;
import org.jzb.J;
import org.jzb.execution.domain.data.FieldValue;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.jzb.Constant.MAPPER;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_ENLISTFEEDBACK")
@NamedQueries({
        @NamedQuery(name = "EnlistFeedback.queryByEnlist", query = "SELECT o FROM EnlistFeedback o WHERE o.enlist=:enlist AND o.deleted=FALSE "),
        @NamedQuery(name = "EnlistFeedback.queryByCreator", query = "SELECT o FROM EnlistFeedback o WHERE o.creator=:creator AND o.deleted=FALSE ")
})
public class EnlistFeedback extends AbstractLogable implements Comparable<EnlistFeedback> {
    @JsonIgnore
    @ManyToOne
    @NotNull
    private Enlist enlist;
    @PrimaryKeyJoinColumn
    @OneToOne
    private EnlistFeedbackPayment payment;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String note;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "T_ENLISTFEEDBACK_T_ATTACHMENT")
    private Collection<UploadFile> attachments;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fieldsValueString;

    @JsonGetter
    public Collection<FieldValue> getFieldsValue() throws IOException {
        if (J.isBlank(fieldsValueString))
            return null;
        CollectionType t = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, FieldValue.class);
        return MAPPER.readValue(fieldsValueString, t);
    }

    public void setFieldsValue(Collection<FieldValue> fieldsValue) {
        try {
            fieldsValueString = CollectionUtils.isEmpty(fieldsValue) ? null : MAPPER.writeValueAsString(fieldsValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Enlist getEnlist() {
        return enlist;
    }

    public void setEnlist(Enlist enlist) {
        this.enlist = enlist;
    }

    public EnlistFeedbackPayment getPayment() {
        return payment;
    }

    public void setPayment(EnlistFeedbackPayment payment) {
        this.payment = payment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Collection<UploadFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<UploadFile> attachments) {
        this.attachments = attachments;
    }

    public String getFieldsValueString() {
        return fieldsValueString;
    }

    public void setFieldsValueString(String fieldsValueString) {
        this.fieldsValueString = fieldsValueString;
    }

    @Override
    public int compareTo(EnlistFeedback o) {
        return o.getModifyDateTime().compareTo(this.getModifyDateTime());
    }

}

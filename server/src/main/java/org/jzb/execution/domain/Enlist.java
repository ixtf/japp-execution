/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;
import org.jzb.execution.domain.data.EnlistStatus;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_ENLIST")
@NamedQueries({
        @NamedQuery(name = "Enlist.queryByManager", query = "SELECT o FROM Enlist o WHERE :manager MEMBER OF o.paymentMerchant.managers AND o.deleted=FALSE "),
})
public class Enlist extends AbstractLogable {
    @ManyToOne
    @NotNull
    private PaymentMerchant paymentMerchant;
    @NotNull
    private EnlistStatus status = EnlistStatus.RUN;
    @NotBlank
    private String title;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String content;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date startDate = new Date();
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date endDate = new Date();
    @ElementCollection
    private Set<String> tags;
    @ManyToMany
    @JoinTable(name = "T_ENLIST_T_ATTACHMENT")
    private Set<UploadFile> attachments;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String feedbackTemplateString;
    /**
     * 0 代表不限制人数
     */
    @Min(0)
    private int feedbackLimit;
    @Column(precision = 12, scale = 2)
    @Min(0)
    private BigDecimal money;

    @JsonGetter
    public FeedbackTemplate getFeedbackTemplate() {
        return J.isBlank(this.feedbackTemplateString) ? null : FeedbackTemplate.fromSaveString(this.feedbackTemplateString);
    }

    public void setFeedbackTemplate(FeedbackTemplate feedbackTemplate) {
        this.feedbackTemplateString = feedbackTemplate == null ? null : feedbackTemplate.toSaveString();
    }

    @JsonIgnore
    public boolean hasFeedbackLimit() {
        return this.feedbackLimit > 0;
    }

    public boolean isStarted() {
        LocalDate ldStart = J.localDate(startDate);
        return !LocalDate.now().isBefore(ldStart);
    }

    public boolean isFinish() {
        return status == EnlistStatus.FINISH;
    }

    public boolean isManager(Operator operator) {
        return paymentMerchant.isManager(operator);
    }

    public boolean isManager(Principal principal) {
        return paymentMerchant.isManager(principal);
    }

    public PaymentMerchant getPaymentMerchant() {
        return paymentMerchant;
    }

    public void setPaymentMerchant(PaymentMerchant paymentMerchant) {
        this.paymentMerchant = paymentMerchant;
    }

    public EnlistStatus getStatus() {
        return status;
    }

    public void setStatus(EnlistStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<UploadFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<UploadFile> attachments) {
        this.attachments = attachments;
    }

    public String getFeedbackTemplateString() {
        return feedbackTemplateString;
    }

    public void setFeedbackTemplateString(String feedbackTemplateString) {
        this.feedbackTemplateString = feedbackTemplateString;
    }

    public int getFeedbackLimit() {
        return feedbackLimit;
    }

    public void setFeedbackLimit(int feedbackLimit) {
        this.feedbackLimit = feedbackLimit;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "title='" + title + '\'' +
                '}';
    }

}

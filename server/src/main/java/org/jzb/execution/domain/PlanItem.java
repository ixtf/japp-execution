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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_PLANITEM")
public class PlanItem extends AbstractEntity implements Comparable<PlanItem> {
    @JsonIgnore
    @ManyToOne
    @NotNull
    private Plan plan;
    @NotBlank
    private String title;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String content;
    @ManyToMany
    private Collection<Experience> experiences;
    @ElementCollection
    private Set<String> tags;
    @ManyToMany
    @JoinTable(name = "T_PLANITEM_T_ATTACHMENT")
    private Collection<UploadFile> attachments;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String feedbackTemplateString;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String evaluateTemplateString;
    private int sortBy;

    @JsonGetter
    public EvaluateTemplate getEvaluateTemplate() {
        return J.isBlank(this.evaluateTemplateString) ? null : EvaluateTemplate.fromSaveString(this.evaluateTemplateString);
    }

    public void setEvaluateTemplate(EvaluateTemplate evaluateTemplate) {
        this.evaluateTemplateString = evaluateTemplate == null ? null : evaluateTemplate.toSaveString();
    }

    @JsonGetter
    public FeedbackTemplate getFeedbackTemplate() {
        return J.isBlank(this.feedbackTemplateString) ? null : FeedbackTemplate.fromSaveString(this.feedbackTemplateString);
    }

    public void setFeedbackTemplate(FeedbackTemplate feedbackTemplate) {
        this.feedbackTemplateString = feedbackTemplate == null ? null : feedbackTemplate.toSaveString();
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
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

    public Collection<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(Collection<Experience> experiences) {
        this.experiences = experiences;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Collection<UploadFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(Collection<UploadFile> attachments) {
        this.attachments = attachments;
    }

    public String getFeedbackTemplateString() {
        return feedbackTemplateString;
    }

    public void setFeedbackTemplateString(String feedbackTemplateString) {
        this.feedbackTemplateString = feedbackTemplateString;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public String getEvaluateTemplateString() {
        return evaluateTemplateString;
    }

    public void setEvaluateTemplateString(String evaluateTemplateString) {
        this.evaluateTemplateString = evaluateTemplateString;
    }

    @Override
    public int compareTo(PlanItem o) {
        return Integer.compare(o.sortBy, sortBy);
    }
}

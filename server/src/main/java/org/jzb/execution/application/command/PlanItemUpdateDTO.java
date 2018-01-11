package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by jzb on 17-4-15.
 */

public class PlanItemUpdateDTO implements Serializable {
    @NotBlank
    private String title;
    private String content;
    private Set<String> tags;
    private List<EntityDTO> attachments;
    private List<EntityDTO> experiences;
    private EntityDTO feedbackTemplate;
    private EntityDTO evaluateTemplate;

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

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public List<EntityDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EntityDTO> attachments) {
        this.attachments = attachments;
    }

    public EntityDTO getFeedbackTemplate() {
        return feedbackTemplate;
    }

    public void setFeedbackTemplate(EntityDTO feedbackTemplate) {
        this.feedbackTemplate = feedbackTemplate;
    }

    public List<EntityDTO> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<EntityDTO> experiences) {
        this.experiences = experiences;
    }

    public EntityDTO getEvaluateTemplate() {
        return evaluateTemplate;
    }

    public void setEvaluateTemplate(EntityDTO evaluateTemplate) {
        this.evaluateTemplate = evaluateTemplate;
    }
}
package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by jzb on 17-4-15.
 */

public class TaskUpdateCommand implements Serializable {
    private EntityDTO taskGroup;
    @NotBlank
    private String title;
    private String content;
    private List<String> tags;
    private List<EntityDTO> attachments;
    private EntityDTO feedbackTemplate;
    private EntityDTO evaluateTemplate;
    @NotNull
    private Date startDate;

    public EntityDTO getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(EntityDTO taskGroup) {
        this.taskGroup = taskGroup;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
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

    public EntityDTO getEvaluateTemplate() {
        return evaluateTemplate;
    }

    public void setEvaluateTemplate(EntityDTO evaluateTemplate) {
        this.evaluateTemplate = evaluateTemplate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
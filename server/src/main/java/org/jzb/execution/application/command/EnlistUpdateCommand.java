package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class EnlistUpdateCommand implements Serializable {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private Set<String> tags;
    private Set<EntityDTO> attachments;
    @NotNull
    private EntityDTO feedbackTemplate;
    @NotNull
    private Date startDate;
    @Min(0)
    private Integer feedbackLimit;
    @Min(0)
    private BigDecimal money;

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

    public Set<EntityDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<EntityDTO> attachments) {
        this.attachments = attachments;
    }

    public EntityDTO getFeedbackTemplate() {
        return feedbackTemplate;
    }

    public void setFeedbackTemplate(EntityDTO feedbackTemplate) {
        this.feedbackTemplate = feedbackTemplate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getFeedbackLimit() {
        return feedbackLimit;
    }

    public void setFeedbackLimit(Integer feedbackLimit) {
        this.feedbackLimit = feedbackLimit;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}

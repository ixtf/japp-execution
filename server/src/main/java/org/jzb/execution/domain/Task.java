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
import org.jzb.execution.domain.data.TaskStatus;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASK")
public class Task extends AbstractLogable implements Comparable<Task> {
    @ManyToOne
    private TaskGroup taskGroup;
    @ManyToOne
    private Plan plan;
    @ManyToOne
    private Task parent;
    @NotNull
    private TaskStatus status = TaskStatus.RUN;
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
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Operator charger;
    @ManyToMany
    @JoinTable(name = "T_TASK_T_PARTICIPANT")
    private Collection<Operator> participants;
    @ManyToMany
    @JoinTable(name = "T_TASK_T_FOLLOWER")
    private Collection<Operator> followers;
    @ManyToMany
    private Collection<Experience> experiences;
    @ElementCollection
    private Collection<String> tags;
    @ManyToMany
    @JoinTable(name = "T_TASK_T_ATTACHMENT")
    private Collection<UploadFile> attachments;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String feedbackTemplateString;
    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String evaluateTemplateString;

    @JsonIgnore
    public boolean isStarted() {
        LocalDate ldStart = J.localDate(startDate);
        return !LocalDate.now().isBefore(ldStart);
    }

    public Stream<Operator> relateOperators() {
        Stream<Operator> stream = Stream.of(charger);
        stream = Stream.concat(stream, J.emptyIfNull(participants).stream());
        stream = Stream.concat(stream, J.emptyIfNull(followers).stream());
        return stream.distinct();
    }

    public boolean isManager(Operator operator) {
        return Stream.concat(Stream.of(charger), J.emptyIfNull(followers).stream())
                .filter(it -> Objects.equals(it, operator))
                .findFirst()
                .isPresent();
    }

    public boolean isManager(Principal principal) {
        return Stream.concat(Stream.of(charger), J.emptyIfNull(followers).stream())
                .map(Operator::getId)
                .filter(it -> Objects.equals(it, principal.getName()))
                .findFirst()
                .isPresent();
    }

    public boolean isParticipant(Principal principal) {
        return J.emptyIfNull(participants)
                .stream()
                .map(Operator::getId)
                .filter(it -> Objects.equals(it, principal.getName()))
                .findFirst()
                .isPresent();
    }

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

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        this.parent = parent;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
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

    public Operator getCharger() {
        return charger;
    }

    public void setCharger(Operator charger) {
        this.charger = charger;
    }

    public Collection<Operator> getParticipants() {
        return participants;
    }

    public void setParticipants(Collection<Operator> participants) {
        this.participants = participants;
    }

    public Collection<Operator> getFollowers() {
        return followers;
    }

    public void setFollowers(Collection<Operator> followers) {
        this.followers = followers;
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

    public void setTags(Collection<String> tags) {
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

    public String getEvaluateTemplateString() {
        return evaluateTemplateString;
    }

    public void setEvaluateTemplateString(String evaluateTemplateString) {
        this.evaluateTemplateString = evaluateTemplateString;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                '}';
    }

    public boolean isFinish() {
        return status == TaskStatus.FINISH;
    }

    @Override
    public int compareTo(Task o) {
        return this.getModifyDateTime().compareTo(o.getModifyDateTime());
    }
}

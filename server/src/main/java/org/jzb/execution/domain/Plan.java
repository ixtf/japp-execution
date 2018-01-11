/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_PLAN")
@NamedQueries({
        @NamedQuery(name = "Plan.find", query = "SELECT o FROM Plan o WHERE o.deleted=FALSE")
})
public class Plan extends AbstractLogable {
    @ManyToOne
    @NotNull
    private Channel channel;
    @NotBlank
    private String name;
    @URL
    private String adUrl;
    @Lob
    private String note;
    @ElementCollection
    private Set<String> tags;
    @ManyToOne
    private Operator publisher;
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    private boolean published = false;
    private boolean audited = false;
    //是否出现在分享平台
    private boolean shared = true;
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    @Size(min = 1)
    private Collection<PlanItem> items;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Operator getPublisher() {
        return publisher;
    }

    public void setPublisher(Operator publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isAudited() {
        return audited;
    }

    public void setAudited(boolean audited) {
        this.audited = audited;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public Collection<PlanItem> getItems() {
        return items;
    }

    public void setItems(Collection<PlanItem> items) {
        this.items = items;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }
}

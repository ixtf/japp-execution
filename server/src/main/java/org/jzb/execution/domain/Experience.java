/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_EXPERIENCE")
public class Experience extends AbstractLogable {
    @NotBlank
    private String name;
    @NotBlank
    @Lob
    private String content;
    private boolean share = false;
    @ElementCollection
    private Set<String> tags;
    @ManyToMany
    @JoinTable(name = "T_EXPERIENCE_T_ATTACHMENT")
    private Collection<UploadFile> attachments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public Set<String> getTags() {
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
}

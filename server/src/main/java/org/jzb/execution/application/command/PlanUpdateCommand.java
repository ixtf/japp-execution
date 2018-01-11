package org.jzb.execution.application.command;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by jzb on 17-4-15.
 */
public class PlanUpdateCommand implements Serializable {
    @NotNull
    private EntityDTO channel;
    @NotBlank
    private String name;
    @URL
    private String adUrl;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String note;
    private Set<String> tags;
    private boolean justQrcode;
    @NotNull
    @Size(min = 1)
    private List<PlanItemUpdateDTO> items;

    public EntityDTO getChannel() {
        return channel;
    }

    public void setChannel(EntityDTO channel) {
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

    public boolean isJustQrcode() {
        return justQrcode;
    }

    public void setJustQrcode(boolean justQrcode) {
        this.justQrcode = justQrcode;
    }

    public List<PlanItemUpdateDTO> getItems() {
        return items;
    }

    public void setItems(List<PlanItemUpdateDTO> items) {
        this.items = items;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }
}
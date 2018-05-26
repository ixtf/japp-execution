/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_TASKGROUP")
public class TaskGroup extends AbstractLogable implements Comparable<TaskGroup> {
    @NotBlank
    private String name;
    @ManyToOne
    private UploadFile logo;
    @ManyToOne
    private UploadFile sign;
    @Lob
    private String signString;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UploadFile getLogo() {
        return logo;
    }

    public void setLogo(UploadFile logo) {
        this.logo = logo;
    }

    public UploadFile getSign() {
        return sign;
    }

    public void setSign(UploadFile sign) {
        this.sign = sign;
    }

    public String getSignString() {
        return signString;
    }

    public void setSignString(String signString) {
        this.signString = signString;
    }

    @Override
    public int compareTo(TaskGroup o) {
        return this.getModifyDateTime().compareTo(o.getModifyDateTime());
    }
}

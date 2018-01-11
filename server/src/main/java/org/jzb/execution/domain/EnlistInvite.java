/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_ENLISTINVITE")
public class EnlistInvite extends WeixinInvite {
    @JsonIgnore
    @ManyToOne
    @NotNull
    private Enlist enlist;

    public Enlist getEnlist() {
        return enlist;
    }

    public void setEnlist(Enlist enlist) {
        this.enlist = enlist;
    }
}

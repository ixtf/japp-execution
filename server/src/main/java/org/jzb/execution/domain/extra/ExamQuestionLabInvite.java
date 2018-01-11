/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain.extra;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jzb.execution.domain.WeixinInvite;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author jzb
 */
@Entity
@Table(name = "T_EXAMQUESTIONLABINVITE")
public class ExamQuestionLabInvite extends WeixinInvite {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private ExamQuestionLab lab;

    public ExamQuestionLab getLab() {
        return lab;
    }

    public void setLab(ExamQuestionLab lab) {
        this.lab = lab;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import org.jzb.execution.domain.operator.Operator;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.security.Principal;
import java.util.Date;
import java.util.Objects;

/**
 * @author jzb
 */
@MappedSuperclass
public abstract class AbstractLogable extends AbstractEntity {
    @ManyToOne
    private Operator creator;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime;
    @ManyToOne
    private Operator modifier;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDateTime;

    public void _loginfo(Operator operator) {
        if (getCreator() == null) {
            setCreator(operator);
            setCreateDateTime(new Date());
        }
        setModifier(operator);
        setModifyDateTime(new Date());
    }

    public Operator getCreator() {
        return creator;
    }

    public void setCreator(Operator creator) {
        this.creator = creator;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Operator getModifier() {
        return modifier;
    }

    public void setModifier(Operator modifier) {
        this.modifier = modifier;
    }

    public Date getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Date modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public boolean isSelf(Principal principal) {
        return Objects.equals(getCreator().getName(), principal.getName());
    }
}

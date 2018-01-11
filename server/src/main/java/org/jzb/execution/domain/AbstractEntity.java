/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jzb.J;
import org.jzb.share.CURDEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

/**
 * @author jzb
 */
@MappedSuperclass
public abstract class AbstractEntity implements CURDEntity<String> {
    @Id
    @Column(length = 36)
    protected String id;
    protected boolean deleted;
    protected long version;

    @PrePersist
    public void PrePersist() {
        if (J.isBlank(getId())) {
            setId(J.uuid58());
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass() || getId() == null) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) object;
        return Objects.equal(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(getId()).toString();
    }
}

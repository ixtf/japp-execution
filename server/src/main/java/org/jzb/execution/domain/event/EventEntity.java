package org.jzb.execution.domain.event;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.domain.AbstractEntity;
import org.jzb.execution.domain.operator.Operator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by jzb on 17-4-15.
 */
@MappedSuperclass
public abstract class EventEntity extends AbstractEntity {
    @NotBlank
    @Column(length = 36)
    protected String entityId;
    @NotNull
    @ManyToOne
    protected Operator operator;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dateTime;
    @Lob
    protected String command;
    @NotNull
    @Enumerated(EnumType.STRING)
    protected EventType eventType;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}

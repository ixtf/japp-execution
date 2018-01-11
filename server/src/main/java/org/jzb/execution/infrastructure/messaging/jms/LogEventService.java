package org.jzb.execution.infrastructure.messaging.jms;

import org.jzb.execution.domain.event.EventEntity;
import org.jzb.execution.domain.event.EventType;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.EventRepository;
import org.jzb.execution.domain.repository.OperatorRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.MapMessage;
import java.util.Date;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
public class LogEventService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private EventRepository eventRepository;

    public <T extends EventEntity> T save(T event, MapMessage msg) throws Exception {
        event.setDateTime(new Date());
        event.setEntityId(msg.getString("id"));
        event.setCommand(msg.getString("command"));
        EventType eventType = EventType.valueOf(msg.getString("eventType"));
        event.setEventType(eventType);
        Operator operator = operatorRepository.find(msg.getString("principal"));
        event.setOperator(operator);
        return (T) eventRepository.save(event);
    }

}

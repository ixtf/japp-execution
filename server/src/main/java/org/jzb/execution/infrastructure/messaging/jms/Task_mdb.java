package org.jzb.execution.infrastructure.messaging.jms;

import org.jboss.resteasy.plugins.server.embedded.SimplePrincipal;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.event.EventType;
import org.jzb.execution.domain.event.TaskEvent;
import org.jzb.execution.domain.repository.TaskRepository;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.security.Principal;
import java.util.Objects;

/**
 * Created by jzb on 17-4-15.
 */

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = JmsApplicationEvents.TOPIC),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class Task_mdb implements MessageListener {
    @Resource
    private MessageDrivenContext mdc;
    @Inject
    private Logger log;
    @Inject
    private LogEventService logEventService;
    @Inject
    private ContextDataService contextDataService;
    @Inject
    private TaskRepository taskRepository;

    @Override
    public void onMessage(Message inMessage) {
        try {
            MapMessage msg = (MapMessage) inMessage;
            String entityClass = msg.getString("entityClass");
            if (!Objects.equals(entityClass, Task.class.getName())) {
                return;
            }

            TaskEvent event = logEventService.save(new TaskEvent(), msg);

            Task task = taskRepository.find(event.getEntityId());
            Principal principal = new SimplePrincipal(msg.getString("principal"));
            EventType eventType = EventType.valueOf(msg.getString("eventType"));
            switch (eventType) {
                case CREATE: {
                    contextDataService.taskCreated(principal, task);
                    break;
                }
                case UPDATE: {
                    contextDataService.taskUpdated(principal, task);
                    break;
                }
                case READ: {
                    contextDataService.taskReaded(principal, task);
                    break;
                }
            }
        } catch (Throwable e) {
            log.error(inMessage + "", e);
        }
    }

}

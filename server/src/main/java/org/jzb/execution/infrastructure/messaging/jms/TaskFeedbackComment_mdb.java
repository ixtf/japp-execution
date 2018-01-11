package org.jzb.execution.infrastructure.messaging.jms;

import org.jboss.resteasy.plugins.server.embedded.SimplePrincipal;
import org.jzb.execution.domain.TaskFeedbackComment;
import org.jzb.execution.domain.event.EventType;
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
public class TaskFeedbackComment_mdb implements MessageListener {
    @Resource
    private MessageDrivenContext mdc;
    @Inject
    private Logger log;
    @Inject
    private ContextDataService contextDataService;

    @Override
    public void onMessage(Message inMessage) {
        try {
            MapMessage msg = (MapMessage) inMessage;
            String entityClass = msg.getString("entityClass");
            if (!Objects.equals(entityClass, TaskFeedbackComment.class.getName())) {
                return;
            }

            Principal principal = new SimplePrincipal(msg.getString("principal"));
            String taskFeedbackCommentId = msg.getString("id");
            EventType eventType = EventType.valueOf(msg.getString("eventType"));
            switch (eventType) {
                case CREATE: {
                    contextDataService.taskFeedbackCommentCreated(principal, taskFeedbackCommentId);
                    break;
                }
                case UPDATE: {
                    contextDataService.taskFeedbackCommentUpdated(principal, taskFeedbackCommentId);
                    break;
                }
                case DELETE: {
                    contextDataService.taskFeedbackCommentDeleted(principal, taskFeedbackCommentId);
                    break;
                }
            }
        } catch (Throwable e) {
            log.error(inMessage + "", e);
        }
    }

}

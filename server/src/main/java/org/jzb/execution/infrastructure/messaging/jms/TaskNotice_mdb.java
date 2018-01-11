package org.jzb.execution.infrastructure.messaging.jms;

import org.jboss.resteasy.plugins.server.embedded.SimplePrincipal;
import org.jzb.execution.application.NoticeService;
import org.jzb.execution.domain.TaskNotice;
import org.jzb.execution.domain.event.EventType;
import org.jzb.execution.domain.repository.TaskNoticeRepository;
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
public class TaskNotice_mdb implements MessageListener {
    @Resource
    private MessageDrivenContext mdc;
    @Inject
    private Logger log;
    @Inject
    private LogEventService logEventService;
    @Inject
    private TaskNoticeRepository taskNoticeRepository;
    @Inject
    private NoticeService noticeService;

    @Override
    public void onMessage(Message inMessage) {
        try {
            MapMessage msg = (MapMessage) inMessage;
            String entityClass = msg.getString("entityClass");
            if (!Objects.equals(entityClass, TaskNotice.class.getName())) {
                return;
            }

            EventType eventType = EventType.valueOf(msg.getString("eventType"));
            TaskNotice taskNotice = taskNoticeRepository.find(msg.getString("id"));
            Principal principal = new SimplePrincipal(msg.getString("principal"));
            log.debug("=========TaskNotice_mdb===========");
            noticeService.schedule(principal, taskNotice);
        } catch (Throwable e) {
            log.error(inMessage + "", e);
        }
    }

}

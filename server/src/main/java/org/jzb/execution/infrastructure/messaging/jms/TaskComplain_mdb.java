package org.jzb.execution.infrastructure.messaging.jms;

import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskComplain;
import org.jzb.execution.domain.event.EventType;
import org.jzb.execution.domain.repository.TaskComplainRepository;
import org.jzb.weixin.mp.MpClient;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Objects;

/**
 * Created by jzb on 17-4-15.
 */

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = JmsApplicationEvents.TOPIC),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class TaskComplain_mdb implements MessageListener {
    @Resource
    private MessageDrivenContext mdc;
    @Inject
    private Logger log;
    @Inject
    private TaskComplainRepository taskComplainRepository;
    @Inject
    private MpClient mpClient;

    @Override
    public void onMessage(Message inMessage) {
        try {
            final MapMessage msg = (MapMessage) inMessage;
            final String entityClass = msg.getString("entityClass");
            if (!Objects.equals(entityClass, TaskComplain.class.getName())) {
                return;
            }

            final EventType eventType = EventType.valueOf(msg.getString("eventType"));
            final TaskComplain taskComplain = taskComplainRepository.find(msg.getString("id"));
            final Task task = taskComplain.getTask();
            switch (eventType) {
                case CREATE:
                case UPDATE: {
                    final String content = "任务：" + task.getTitle() + "被用户【" + taskComplain.getModifier().getName() + "】投诉！";
                    mpClient.msgKf("oohBQwjH5QPqyNVFzf54MTMSBV0Y").text().content(content).call();
                    // mpClient.msgKf("oohBQwnqvCd_cZHeTW5uXQxzw6Ek").text().content(content).call();
                    break;
                }
            }
        } catch (Throwable e) {
            log.error(inMessage + "", e);
        }
    }

}

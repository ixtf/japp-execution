package org.jzb.execution.infrastructure.messaging.jms;

import org.jzb.J;
import org.jzb.execution.application.ApplicationEvents;
import org.jzb.execution.domain.event.EventType;
import org.jzb.weixin.mp.MsgPushed;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.*;
import java.security.Principal;


/**
 * Created by jzb on 17-4-15.
 */

@ApplicationScoped
public class JmsApplicationEvents implements ApplicationEvents {
    public static final String QUEUE = "java:/jms/japp-execution-Queue";
    public static final String TOPIC = "java:/jms/japp-execution-Topic";
    @Inject
    private JMSContext jmsContext;
    @Resource(mappedName = QUEUE)
    private Queue queue;
    @Resource(mappedName = TOPIC)
    private Topic topic;

    @Override
    public void fireWeixinMsgPush(String xml) throws JMSException {
        MapMessage message = jmsContext.createMapMessage();
        message.setString("entityClass", MsgPushed.class.getName());
        message.setString("xml", xml);
        jmsContext.createProducer().send(topic, message);
    }

    @Override
    public void fireCurd(Principal principal, Class clazz, String id, EventType eventType, Object command) throws Exception {
        MapMessage message = jmsContext.createMapMessage();
        message.setString("principal", principal.getName());
        message.setString("entityClass", clazz.getName());
        message.setObject("id", id);
        message.setString("eventType", eventType.name());
        message.setString("command", J.toJson(command));
        jmsContext.createProducer().send(topic, message);
    }

    @Override
    public void fireCurd(Principal principal, Class clazz, String id, EventType eventType) throws Exception {
        MapMessage message = jmsContext.createMapMessage();
        message.setString("principal", principal.getName());
        message.setString("entityClass", clazz.getName());
        message.setObject("id", id);
        message.setString("eventType", eventType.name());
        jmsContext.createProducer().send(topic, message);
    }
}

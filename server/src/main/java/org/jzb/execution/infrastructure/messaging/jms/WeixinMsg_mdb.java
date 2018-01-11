package org.jzb.execution.infrastructure.messaging.jms;

import org.jzb.weixin.mp.MsgPushed;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
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
public class WeixinMsg_mdb implements MessageListener {
    @Resource
    private MessageDrivenContext mdc;
    @Resource
    private ManagedScheduledExecutorService ses;
    @Inject
    private Logger log;
    @Inject
    private WeixinMsgService weixinMsgService;

    @Override
    public void onMessage(Message inMessage) {
        try {
            MapMessage msg = (MapMessage) inMessage;
            String entityClass = msg.getString("entityClass");
            if (!Objects.equals(entityClass, MsgPushed.class.getName())) {
                return;
            }
            MsgPushed msgPushed = new MsgPushed(msg.getString("xml"));
            weixinMsgService.handle(msgPushed);
        } catch (Throwable e) {
            log.error(inMessage + "", e);
        }
    }

}

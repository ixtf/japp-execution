package org.jzb.execution.interfaces.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.execution.interfaces.websocket.config.JsonDecoder;
import org.jzb.execution.interfaces.websocket.config.JsonEncoder;
import org.jzb.execution.interfaces.websocket.config.MyConfigurator;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by jzb on 17-4-15.
 */

@ServerEndpoint(value = "/global", configurator = MyConfigurator.class, encoders = JsonEncoder.class, decoders = JsonDecoder.class)
public class GlobalEndpoint {
    private static final Queue<Session> queue = new ConcurrentLinkedQueue<>();
    @Inject
    private Logger log;

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        session.getUserProperties().put("principal", conf.getUserProperties().get("principal"));
        queue.add(session);
    }

    @OnMessage
    public void onMessage(JsonNode node, Session session) {
    }

    @OnClose
    public void close(Session session) {
        queue.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        close(session);
        log.error("", t);
    }
}

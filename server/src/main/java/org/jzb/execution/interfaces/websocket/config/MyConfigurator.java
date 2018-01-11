package org.jzb.execution.interfaces.websocket.config;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public class MyConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig conf, HandshakeRequest req, HandshakeResponse resp) {
        Principal principal = req.getUserPrincipal();
        conf.getUserProperties().put("handshakeRequest", req);
        conf.getUserProperties().put("principal", principal);
    }
}

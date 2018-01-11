package org.jzb.execution.interfaces.websocket.config;

import org.jzb.J;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by jzb on 17-4-15.
 */
public class JsonEncoder implements Encoder.Text<Object> {
    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(Object object) throws EncodeException {
        return J.toJson(object);
    }
}
package org.jzb.execution.interfaces.websocket.config;

import com.fasterxml.jackson.databind.JsonNode;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
public class JsonDecoder implements Decoder.Text<JsonNode> {
    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public JsonNode decode(String s) throws DecodeException {
        try {
            return MAPPER.readTree(s);
        } catch (IOException e) {
            throw new DecodeException(s, "", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}

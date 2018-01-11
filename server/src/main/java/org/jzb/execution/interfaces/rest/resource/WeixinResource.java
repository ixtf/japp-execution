package org.jzb.execution.interfaces.rest.resource;

import org.jzb.execution.application.command.SimpleStringDTO;
import org.jzb.weixin.mp.MpClient;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("weixin")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class WeixinResource {
    @Inject
    private MpClient mpClient;

    @Path("jsConfig")
    @POST
    public Map<String, String> wxJsConfig(@Valid @NotNull SimpleStringDTO dto) throws Exception {
        return mpClient.jsConfig(dto.getData());
    }

}

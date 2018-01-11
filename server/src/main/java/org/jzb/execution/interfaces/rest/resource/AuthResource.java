package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.command.LoginCommand;
import org.jzb.execution.application.command.SimpleStringDTO;
import org.jzb.execution.domain.operator.Login;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.weixin.mp.MpClient;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.jzb.Constant.MAPPER;
import static org.jzb.execution.Constant.ADMIN_IDS;
import static org.jzb.execution.Constant.ROOT_URL;

/**
 * Created by jzb on 17-4-15.
 */
@Path("auth")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class AuthResource {
    @Inject
    private MpClient mpClient;
    @Inject
    private OperatorRepository operatorRepository;

    @POST
    public JsonNode login(@Valid @NotNull LoginCommand command) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        subject.login(command.getShiroToken());
        Login login = operatorRepository.findLoginByLoginId(command.getLoginId());
        Operator operator = login.getOperator();
        boolean isAdmin = ADMIN_IDS.contains(operator.getId());
        return MAPPER.createObjectNode().put("isAdmin", isAdmin)
                .set("operator", MAPPER.getNodeFactory().pojoNode(operator));
    }

    @GET
    public JsonNode get(@Context SecurityContext sc) {
        Operator operator = operatorRepository.find(sc.getUserPrincipal());
        boolean isAdmin = ADMIN_IDS.contains(operator.getId());
        return MAPPER.createObjectNode().put("isAdmin", isAdmin)
                .set("operator", MAPPER.getNodeFactory().pojoNode(operator));
    }

    @DELETE
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Path("logout")
    @GET
    public Response logout(@Context SecurityContext sc) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Response.seeOther(URI.create(ROOT_URL)).build();
    }

    @Path("wxJsConfig")
    @POST
    public Map<String, String> wxJsConfig(@Valid @NotNull SimpleStringDTO dto) throws Exception {
        return mpClient.jsConfig(dto.getData());
    }

    @Path("weixin_mp_access_token")
    @GET
    @Produces(TEXT_PLAIN)
    public String weixin_mp_access_token(@Valid @NotBlank @QueryParam("u") String u,
                                         @Valid @NotBlank @QueryParam("p") String p) throws Exception {
        if (u.equals("admin") && p.equals("tomking")) {
            return mpClient.access_token();
        }
        return "";
    }
}

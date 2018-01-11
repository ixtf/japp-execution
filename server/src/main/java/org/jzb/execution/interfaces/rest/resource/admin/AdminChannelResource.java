package org.jzb.execution.interfaces.rest.resource.admin;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.AdminService;
import org.jzb.execution.application.command.ChannelUpdateCommand;
import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.repository.ChannelRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class AdminChannelResource {
    @Inject
    private ChannelRepository channelRepository;
    @Inject
    private AdminService adminService;

    @POST
    public Channel create(@Context SecurityContext sc, @Valid @NotNull ChannelUpdateCommand command) {
        return adminService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public Channel update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull ChannelUpdateCommand command) {
        return adminService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @DELETE
    public void update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        adminService.deleteChannel(sc.getUserPrincipal(), id);
    }

}

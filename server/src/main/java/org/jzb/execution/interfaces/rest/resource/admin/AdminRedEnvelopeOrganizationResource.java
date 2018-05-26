package org.jzb.execution.interfaces.rest.resource.admin;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.RedEnvelopeOrganizationService;
import org.jzb.execution.application.command.RedEnvelopeOrganizationUpdateCommand;
import org.jzb.execution.domain.RedEnvelopeOrganization;
import org.jzb.execution.domain.repository.RedEnvelopeOrganizationRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class AdminRedEnvelopeOrganizationResource {
    @Inject
    private RedEnvelopeOrganizationRepository redEnvelopeOrganizationRepository;
    @Inject
    private RedEnvelopeOrganizationService redEnvelopeOrganizationService;

    @POST
    public RedEnvelopeOrganization create(@Context SecurityContext sc, @Valid @NotNull RedEnvelopeOrganizationUpdateCommand command) {
        return redEnvelopeOrganizationService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public RedEnvelopeOrganization update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull RedEnvelopeOrganizationUpdateCommand command) {
        return redEnvelopeOrganizationService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public RedEnvelopeOrganization get(@Valid @NotBlank @PathParam("id") String id) {
        return redEnvelopeOrganizationRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        redEnvelopeOrganizationService.delete(sc.getUserPrincipal(), id);
    }

    @GET
    public Collection<RedEnvelopeOrganization> list() {
        return redEnvelopeOrganizationRepository.findAll().collect(Collectors.toList());
    }

    @Path("{id}/managers/{managerId}")
    @PUT
    public void addManager(@Context SecurityContext sc,
                           @Valid @NotBlank @PathParam("id") String id,
                           @Valid @NotBlank @PathParam("managerId") String managerId) {
        redEnvelopeOrganizationService.addManager(sc.getUserPrincipal(), id, managerId);
    }

    @Path("{id}/managers/{managerId}")
    @DELETE
    public void removeManager(@Context SecurityContext sc,
                              @Valid @NotBlank @PathParam("id") String id,
                              @Valid @NotBlank @PathParam("managerId") String managerId) {
        redEnvelopeOrganizationService.removeManager(sc.getUserPrincipal(), id, managerId);
    }

}

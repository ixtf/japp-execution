package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.RedEnvelopeOrganizationService;
import org.jzb.execution.domain.RedEnvelopeOrganization;
import org.jzb.execution.domain.RedEnvelopeOrganizationInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.RedEnvelopeOrganizationRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("redEnvelopeOrganizations")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class RedEnvelopeOrganizationResource {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private RedEnvelopeOrganizationRepository redEnvelopeOrganizationRepository;
    @Inject
    private RedEnvelopeOrganizationService redEnvelopeOrganizationService;

    @GET
    public Collection<RedEnvelopeOrganization> list(@Context SecurityContext sc) {
        final Operator manager = operatorRepository.find(sc.getUserPrincipal());
        return redEnvelopeOrganizationRepository.queryByManager(manager).collect(Collectors.toList());
    }

    @Path("{id}")
    @GET
    public RedEnvelopeOrganization get(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return redEnvelopeOrganizationRepository.find(id);
    }

    @Path("{id}/inviteTicket")
    @GET
    public RedEnvelopeOrganizationInvite inviteToken(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return redEnvelopeOrganizationService.inviteTicket(sc.getUserPrincipal(), id);
    }

    @Path("{id}/managers/{managerId}")
    @DELETE
    public void get(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotBlank @PathParam("managerId") String managerId) throws Exception {
        redEnvelopeOrganizationService.removeManager(sc.getUserPrincipal(), id, managerId);
    }
}

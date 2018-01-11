package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.EnlistService;
import org.jzb.execution.application.command.EnlistGenerateTaskCommand;
import org.jzb.execution.application.command.EnlistUpdateCommand;
import org.jzb.execution.domain.Enlist;
import org.jzb.execution.domain.EnlistFeedback;
import org.jzb.execution.domain.EnlistInvite;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.repository.EnlistFeedbackRepository;
import org.jzb.execution.domain.repository.EnlistRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
@Path("enlists")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class EnlistResource {
    @Inject
    private EnlistRepository enlistRepository;
    @Inject
    private EnlistFeedbackRepository enlistFeedbackRepository;
    @Inject
    private EnlistService enlistService;

    @POST
    public Enlist create(@Context SecurityContext sc,
                         @Valid @NotBlank @QueryParam("paymentMerchantId") String paymentMerchantId,
                         @Valid @NotNull EnlistUpdateCommand command) throws Exception {
        return enlistService.create(sc.getUserPrincipal(), paymentMerchantId, command);
    }

    @GET
    public Collection<Enlist> list(@Context SecurityContext sc) {
        return enlistRepository.query(sc.getUserPrincipal())
                .filter(enlist -> !enlist.isFinish())
                .collect(Collectors.toSet());
    }

    @Path("{id}")
    @PUT
    public Enlist update(@Context SecurityContext sc,
                         @Valid @NotBlank @PathParam("id") String id,
                         @Valid @NotNull EnlistUpdateCommand command) throws Exception {
        return enlistService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public Enlist get(@Valid @NotBlank @PathParam("id") String id) {
        return enlistRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        enlistService.delete(sc.getUserPrincipal(), id);
    }

    @Path("{id}/finish")
    @PUT
    public void finish(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        enlistService.finish(sc.getUserPrincipal(), id);
    }

    @Path("{id}/finish")
    @DELETE
    public void restart(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        enlistService.restart(sc.getUserPrincipal(), id);
    }

    @Path("{id}/task")
    @POST
    public Task generateTask(@Context SecurityContext sc,
                             @Valid @NotBlank @PathParam("id") String id,
                             @Valid @NotNull EnlistGenerateTaskCommand command) throws Exception {
        return enlistService.generateTask(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}/inviteTicket")
    @GET
    public EnlistInvite inviteToken(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return enlistService.inviteTicket(sc.getUserPrincipal(), id);
    }

    @Path("{id}/enlistFeedbacks")
    @GET
    public Collection<EnlistFeedback> addManager(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        final Principal principal = sc.getUserPrincipal();
        final Enlist enlist = enlistRepository.find(id);
        return enlistFeedbackRepository.queryBy(enlist)
                .filter(enlistFeedback -> {
                    if (enlistFeedback.getEnlist().isManager(principal)) {
                        return true;
                    } else {
                        return Objects.equals(enlistFeedback.getCreator().getId(), principal.getName());
                    }
                })
                .collect(Collectors.toList());
    }

}

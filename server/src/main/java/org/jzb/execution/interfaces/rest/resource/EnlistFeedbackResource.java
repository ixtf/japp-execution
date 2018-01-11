package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.EnlistFeedbackService;
import org.jzb.execution.application.command.EnlistFeedbackUpdateCommand;
import org.jzb.execution.domain.EnlistFeedback;
import org.jzb.execution.domain.repository.EnlistFeedbackRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("enlistFeedbacks")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class EnlistFeedbackResource {
    @Inject
    private EnlistFeedbackRepository enlistFeedbackRepository;
    @Inject
    private EnlistFeedbackService enlistFeedbackService;

    @POST
    public EnlistFeedback create(@Context SecurityContext sc,
                                 @Valid @NotBlank @QueryParam("enlistId") String enlistId,
                                 @Valid @NotNull EnlistFeedbackUpdateCommand command) throws Exception {
        return enlistFeedbackService.create(sc.getUserPrincipal(), enlistId, command);
    }

    @Path("{id}")
    @PUT
    public EnlistFeedback update(@Context SecurityContext sc,
                                 @Valid @NotBlank @PathParam("id") String id,
                                 @Valid @NotNull EnlistFeedbackUpdateCommand command) throws Exception {
        return enlistFeedbackService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public EnlistFeedback get(@Valid @NotBlank @PathParam("id") String id) {
        return enlistFeedbackRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        enlistFeedbackService.delete(sc.getUserPrincipal(), id);
    }

    @Path("{id}/weixinPay")
    @GET
    public Map<String, String> weixinPaySl(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return enlistFeedbackService.weixinPaySl(sc.getUserPrincipal(), id);
    }

    // @Path("{id}/weixinPayMsg")
    // @PUT
    // public void sendPayMsg(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
    //     enlistFeedbackService.sendPrePayMsg(sc.getUserPrincipal(), id);
    // }

}

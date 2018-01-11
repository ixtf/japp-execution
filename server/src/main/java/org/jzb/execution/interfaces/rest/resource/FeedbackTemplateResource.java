package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.FeedbackTemplateService;
import org.jzb.execution.application.command.FeedbackTemplateUpdateCommand;
import org.jzb.execution.domain.FeedbackTemplate;
import org.jzb.execution.domain.repository.FeedbackTemplateRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("feedbackTemplates")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class FeedbackTemplateResource {
    @Inject
    private FeedbackTemplateRepository feedbackTemplateRepository;
    @Inject
    private FeedbackTemplateService feedbackTemplateService;

    @POST
    public Response create(@Context SecurityContext sc, @Valid @NotNull FeedbackTemplateUpdateCommand command) throws Exception {
        FeedbackTemplate result = feedbackTemplateService.create(sc.getUserPrincipal(), command);
        return Response.ok(result).build();
    }

    @Path("{id}")
    @PUT
    public Response update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull FeedbackTemplateUpdateCommand command) throws Exception {
        FeedbackTemplate result = feedbackTemplateService.update(sc.getUserPrincipal(), id, command);
        return Response.ok(result).build();
    }

    @Path("{id}")
    @GET
    public Response update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        FeedbackTemplate result = feedbackTemplateRepository.find(id);
        return Response.ok(result).build();
    }

    @GET
    public Collection<FeedbackTemplate> get(@Context SecurityContext sc) {
        return feedbackTemplateRepository.query(sc.getUserPrincipal()).collect(Collectors.toList());
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        feedbackTemplateService.delete(sc.getUserPrincipal(), id);
    }

}

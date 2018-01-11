package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.EvaluateTemplateService;
import org.jzb.execution.application.command.EvaluateTemplateUpdateCommand;
import org.jzb.execution.domain.EvaluateTemplate;
import org.jzb.execution.domain.repository.EvaluateTemplateRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("evaluateTemplates")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class EvaluateTemplateResource {
    @Inject
    private EvaluateTemplateRepository evaluateTemplateRepository;
    @Inject
    private EvaluateTemplateService evaluateTemplateService;

    @POST
    public EvaluateTemplate create(@Context SecurityContext sc, @Valid @NotNull EvaluateTemplateUpdateCommand command) throws Exception {
        return evaluateTemplateService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public EvaluateTemplate update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull EvaluateTemplateUpdateCommand command) throws Exception {
        return evaluateTemplateService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public EvaluateTemplate get(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        return evaluateTemplateRepository.find(id);
    }

    @GET
    public Collection<EvaluateTemplate> get(@Context SecurityContext sc) {
        return evaluateTemplateRepository.query(sc.getUserPrincipal());
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        evaluateTemplateService.delete(sc.getUserPrincipal(), id);
    }

}

package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.ApplicationEvents;
import org.jzb.execution.application.ExamService;
import org.jzb.execution.application.command.ExamUpdateCommand;
import org.jzb.execution.domain.extra.Exam;
import org.jzb.execution.domain.repository.ExamRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("exams")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class ExamResource {
    @Inject
    private ExamRepository examRepository;
    @Inject
    private ExamService examService;
    @Inject
    private ApplicationEvents applicationEvents;

    @POST
    public Response create(@Context SecurityContext sc, @Valid @NotNull ExamUpdateCommand command) {
        Exam result = examService.create(sc.getUserPrincipal(), command);
        return Response.ok(result).build();
    }

    @Path("{id}")
    @PUT
    public Response update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull ExamUpdateCommand command) {
        Exam result = examService.update(sc.getUserPrincipal(), id, command);
        return Response.ok(result).build();
    }

    @Path("{id}")
    @GET
    public Response update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        Exam result = examRepository.find(id);
        return Response.ok(result).build();
    }

}

package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.Util;
import org.jzb.execution.application.ExamQuestionService;
import org.jzb.execution.application.command.ExamQuestionUpdateCommand;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.repository.ExamQuestionRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;

/**
 * Created by jzb on 17-4-15.
 */
@Path("examQuestions")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class ExamQuestionResource {
    @Inject
    private ExamQuestionRepository examQuestionRepository;
    @Inject
    private ExamQuestionService examQuestionService;

    @POST
    public ExamQuestion create(@Context SecurityContext sc,
                               @QueryParam("labId") String labId,
                               @Valid @NotNull ExamQuestionUpdateCommand command) {
        return examQuestionService.create(sc.getUserPrincipal(), labId, command);
    }

    @Path("{id}")
    @PUT
    public ExamQuestion update(@Context SecurityContext sc,
                               @QueryParam("labId") String labId,
                               @Valid @NotBlank @PathParam("id") String id,
                               @Valid @NotNull ExamQuestionUpdateCommand command) {
        return examQuestionService.update(sc.getUserPrincipal(), labId, id, command);
    }

    @Path("{id}")
    @GET
    public ExamQuestion update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        return examQuestionRepository.find(id);
    }

    @Path("{id}/question/download")
    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    public Response downloadQuestion(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        ExamQuestion examQuestion = examQuestionRepository.find(id);
        return Util.toDownloadResponse(examQuestion.getQuestion());
    }

    @Path("{id}/answer/download")
    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    public Response downloadAnswer(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        ExamQuestion examQuestion = examQuestionRepository.find(id);
        return Util.toDownloadResponse(examQuestion.getAnswer());
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        examQuestionService.delete(sc.getUserPrincipal(), id);
    }
}

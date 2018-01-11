package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.ExamQuestionLabService;
import org.jzb.execution.application.command.ExamQuestionLabUpdateCommand;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.extra.ExamQuestionLabInvite;
import org.jzb.execution.domain.repository.ExamQuestionLabRepository;
import org.jzb.execution.domain.repository.ExamQuestionRepository;

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
@Path("examQuestionLabs")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class ExamQuestionLabResource {
    @Inject
    private ExamQuestionLabRepository examQuestionLabRepository;
    @Inject
    private ExamQuestionRepository examQuestionRepository;
    @Inject
    private ExamQuestionLabService examQuestionLabService;

    @POST
    public ExamQuestionLab create(@Context SecurityContext sc, @Valid @NotNull ExamQuestionLabUpdateCommand command) {
        return examQuestionLabService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public ExamQuestionLab update(@Context SecurityContext sc,
                                  @Valid @NotBlank @PathParam("id") String id,
                                  @Valid @NotNull ExamQuestionLabUpdateCommand command) {
        return examQuestionLabService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public ExamQuestionLab get(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        return examQuestionLabRepository.find(id);
    }

    @GET
    public Collection<ExamQuestionLab> list(@Context SecurityContext sc) {
        return examQuestionLabRepository.query(sc.getUserPrincipal()).collect(Collectors.toList());
    }

    @Path("{id}/participants/{participantId}")
    @DELETE
    public void deleteParticipant(@Context SecurityContext sc,
                                  @Valid @NotBlank @PathParam("id") String id,
                                  @Valid @NotBlank @PathParam("participantId") String participantId) throws Exception {
        examQuestionLabService.deleteParticipant(sc.getUserPrincipal(), id, participantId);
    }

    @Path("/{id}/inviteTicket")
    @GET
    public ExamQuestionLabInvite inviteToken(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return examQuestionLabService.inviteTicket(sc.getUserPrincipal(), id);
    }

    @Path("{id}/examQuestions")
    @GET
    public Collection<ExamQuestion> list(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        return examQuestionRepository.queryByLabId(sc.getUserPrincipal(), id);
    }
}

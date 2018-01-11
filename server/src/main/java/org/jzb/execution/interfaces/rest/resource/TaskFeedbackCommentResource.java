package org.jzb.execution.interfaces.rest.resource;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.Util;
import org.jzb.execution.application.TaskService;
import org.jzb.execution.domain.UploadFile;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.execution.domain.repository.TaskFeedbackCommentRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.*;

/**
 * Created by jzb on 17-4-15.
 */
@Path("taskFeedbackComments")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskFeedbackCommentResource {
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;
    @Inject
    private TaskService taskService;

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        taskService.deleteTaskFeedbackComment(sc.getUserPrincipal(), id);
    }

    @Path("{id}/attachments/{attachmentId}/download")
    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    public Response question(@Context SecurityContext sc,
                             @Valid @NotBlank @PathParam("id") String id,
                             @Valid @NotBlank @PathParam("attachmentId") String attachmentId) throws Exception {
        UploadFile uploadFile = taskFeedbackCommentRepository.find(id)
                .getAttachments()
                .stream()
                .filter(attachment -> attachment.getId().equals(attachmentId))
                .findFirst()
                .get();
        return Util.toDownloadResponse(uploadFile);
    }

    @Path("{id}/examQuestions/{examQuestionId}/question/download")
    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    public Response downloadQuestion(@Context SecurityContext sc,
                                     @Valid @NotBlank @PathParam("id") String id,
                                     @Valid @NotBlank @PathParam("examQuestionId") String examQuestionId) throws Exception {
        ExamQuestion examQuestion = taskFeedbackCommentRepository.find(id)
                .getExamQuestions()
                .stream()
                .filter(it -> it.getId().equals(examQuestionId))
                .findFirst()
                .get();
        UploadFile uploadFile = examQuestion.getQuestion();
        String fileName = examQuestion.getName() + "—题目." + FilenameUtils.getExtension(uploadFile.getFileName());
        return Util.toDownloadResponse(uploadFile._file(), fileName);
    }

    @Path("{id}/examQuestions/{examQuestionId}/question/html")
    @GET
    @Produces(TEXT_HTML)
    public Response showQuestion(@Context SecurityContext sc,
                                 @Valid @NotBlank @PathParam("id") String id,
                                 @Valid @NotBlank @PathParam("examQuestionId") String examQuestionId) throws Exception {
        UploadFile uploadFile = taskFeedbackCommentRepository.find(id)
                .getExamQuestions()
                .stream()
                .filter(examQuestion -> examQuestion.getId().equals(examQuestionId))
                .findFirst()
                .map(ExamQuestion::getQuestion)
                .get();
        return Util.toHtmlResponse(uploadFile);
    }

    @Path("{id}/examQuestions/{examQuestionId}/answer/download")
    @GET
    @Produces(APPLICATION_OCTET_STREAM)
    public Response downloadAnswer(@Context SecurityContext sc,
                                   @Valid @NotBlank @PathParam("id") String id,
                                   @Valid @NotBlank @PathParam("examQuestionId") String examQuestionId) throws Exception {
        ExamQuestion examQuestion = taskFeedbackCommentRepository.find(id)
                .getExamQuestions()
                .stream()
                .filter(it -> it.getId().equals(examQuestionId))
                .findFirst()
                .get();
        UploadFile uploadFile = examQuestion.getAnswer();
        String fileName = examQuestion.getName() + "—答案." + FilenameUtils.getExtension(uploadFile.getFileName());
        return Util.toDownloadResponse(uploadFile._file(), fileName);
    }

    @Path("{id}/examQuestions/{examQuestionId}/answer/html")
    @GET
    @Produces(TEXT_HTML)
    public Response showAnswer(@Context SecurityContext sc,
                               @Valid @NotBlank @PathParam("id") String id,
                               @Valid @NotBlank @PathParam("examQuestionId") String examQuestionId) throws Exception {
        UploadFile uploadFile = taskFeedbackCommentRepository.find(id)
                .getExamQuestions()
                .stream()
                .filter(examQuestion -> examQuestion.getId().equals(examQuestionId))
                .findFirst()
                .map(ExamQuestion::getAnswer)
                .get();
        return Util.toHtmlResponse(uploadFile);
    }

}

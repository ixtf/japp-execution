package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.hibernate.validator.constraints.NotBlank;
import org.jboss.resteasy.annotations.GZIP;
import org.jzb.J;
import org.jzb.execution.Util;
import org.jzb.execution.application.ExamQuestionService;
import org.jzb.execution.application.TaskService;
import org.jzb.execution.application.command.EntityDTO;
import org.jzb.execution.application.command.NickNamesUpdateCommand;
import org.jzb.execution.application.command.TaskBatchBaseCommand;
import org.jzb.execution.application.command.TasksFollowInviteCommand;
import org.jzb.execution.domain.*;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.TaskFeedbackCommentRepository;
import org.jzb.execution.domain.repository.TaskFeedbackRepository;
import org.jzb.execution.domain.repository.TaskRepository;
import org.jzb.poi.JPoi;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;
import static org.jzb.execution.Constant.JOIN_EXAM_QUESTION_IMAGE_DIR;
import static org.jzb.execution.Constant.UPLOAD_TMP_PATH;

/**
 * Created by jzb on 17-4-15.
 */
@GZIP
@Path("taskBatch")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class TaskBatchResource {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private TaskRepository taskRepository;
    @Inject
    private TaskFeedbackRepository taskFeedbackRepository;
    @Inject
    private TaskFeedbackCommentRepository taskFeedbackCommentRepository;
    @Inject
    private TaskService taskService;
    @Inject
    private ExamQuestionService examQuestionService;

    @Path("taskGroups")
    @GET
    public Collection<TaskGroup> list(@Context SecurityContext sc) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskRepository.query(principal)
                .filter(task -> task.isManager(principal))
                .map(Task::getTaskGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Path("taskGroups/{taskGroupId}/tasks")
    @GET
    public Collection<Task> manageTasks(@Context SecurityContext sc,
                                        @Valid @NotBlank @PathParam("taskGroupId") String taskGroupId) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        return taskRepository.queryByTaskGroupId(principal, taskGroupId)
                .filter(task -> task.isManager(principal))
                .collect(Collectors.toList());
    }

    @Path("nickNames")
    @POST
    public void updateNickNames(@Context SecurityContext sc, @Valid @NotNull NickNamesUpdateCommand command) throws Exception {
        taskService.updateNickNames(sc.getUserPrincipal(), command);
    }

    @Path("finish")
    @POST
    public void finish(@Context SecurityContext sc, @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .distinct()
                .forEach(id -> taskService.finish(principal, id));
    }

    @Path("invite")
    @POST
    public TasksInvite invite(@Context SecurityContext sc, @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        return taskService.inviteTicket(sc.getUserPrincipal(), command);
    }

    @Path("followInvite")
    @POST
    public TasksFollowInvite followInvite(@Context SecurityContext sc, @Valid @NotNull TasksFollowInviteCommand command) throws Exception {
        return taskService.followInviteTicket(sc.getUserPrincipal(), command);
    }

    @Path("joinExamQuestions")
    @POST
    public Response joinExamQuestions(@Context SecurityContext sc, @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();
        File file = examQuestionService.review(principal, command);
        File pdfFile = Util.toPdf(file);
        Operator operator = operatorRepository.find(principal);
        String downloadName = operator.getName() + "—【" + LocalDate.now().toString() + "】错题.pdf";
        JsonNode result = MAPPER.createObjectNode()
                .put("fileName", downloadName)
                .put("downloadToken", Util.downloadToken(principal, pdfFile, "application/pdf", downloadName))
                .put("html", Util.toHtml(file));
        return Response.ok(result).build();
    }

//    @Path("managerJoinExamQuestion")
//    @POST
//    public Response joinExamQuestions(@Context SecurityContext sc,
//                                      @Valid @NotBlank @QueryParam("operatorId") String operatorId,
//                                      @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
//        final Principal principal = sc.getUserPrincipal();
//        File file = examQuestionService.managerReview(principal, operatorId, command);
//        File pdfFile = Util.toPdf(file);
//        Operator operator = operatorRepository.find(operatorId);
//        String downloadName = operator.getName() + "—【" + LocalDate.now().toString() + "】错题.pdf";
//        // String downloadName = "试题—答案【" + LocalDate.now().toString() + "】错题.pdf";
//        JsonNode result = MAPPER.createObjectNode()
//                .put("fileName", downloadName)
//                .put("downloadToken", Util.downloadToken(principal, pdfFile, "application/pdf", downloadName))
//                .put("downloadTokenByDocx", Util.downloadToken(principal, file, "application/doc", downloadName + ".doc"))
//                .put("html", Util.toHtml(file));
//        return Response.ok(result).build();
//    }

    @Path("managerJoinExamQuestion")
    @POST
    public Response joinExamQuestions(@Context SecurityContext sc,
                                      @Valid @NotBlank @QueryParam("operatorId") String operatorId,
                                      @Valid @NotNull TaskBatchBaseCommand command) throws Exception {
        final Principal principal = sc.getUserPrincipal();

        File file = examQuestionService.managerReview(principal, operatorId, command);
        File pdfFile = Util.toPdf(file);
        Operator operator = operatorRepository.find(operatorId);
        String downloadName = operator.getName() + "—【" + LocalDate.now().toString() + "】错题.pdf";
        // String downloadName = "试题—答案【" + LocalDate.now().toString() + "】错题.pdf";

        Collection<File> questions = Lists.newArrayList();
        Collection<File> answers = Lists.newArrayList();
        command.getTasks()
                .stream()
                .map(EntityDTO::getId)
                .map(taskRepository::find)
                .flatMap(taskFeedbackRepository::queryBy)
                .filter(taskFeedback -> taskFeedback.getCreator().getId().equals(operatorId))
                .flatMap(taskFeedbackCommentRepository::queryBy)
                .map(TaskFeedbackComment::getExamQuestions)
                .filter(J::nonEmpty)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(examQuestion -> {
                    UploadFile question = examQuestion.getQuestion();
                    questions.add(question._file());
                    UploadFile answer = examQuestion.getAnswer();
//                    answers.add(question._file());
                    answers.add(answer._file());
                });
        final File questionsFile = get(questions);
        final File answersFile = get(answers);

        JsonNode result = MAPPER.createObjectNode()
                .put("fileName", downloadName)
                .put("downloadToken", Util.downloadToken(principal, pdfFile, "application/pdf", downloadName))
                .put("downloadTokenByQuestion", Util.downloadToken(principal, questionsFile, "application/doc", operator.getName() + "—【" + LocalDate.now().toString() + "】错题.doc"))
                .put("downloadTokenByAnswer", Util.downloadToken(principal, answersFile, "application/doc", operator.getName() + "—【" + LocalDate.now().toString() + "】答案.doc"))
                .put("html", Util.toHtml(file));
        return Response.ok(result).build();
    }

    private File get(Collection<File> files) throws IOException, InvalidFormatException {
        String[] uploadFilePaths = files.stream()
                .map(File::getAbsolutePath)
                .sorted()
                .toArray(size -> new String[size]);
        File result = FileUtils.getFile(UPLOAD_TMP_PATH, J.uuid58(uploadFilePaths) + ".docx");
        if (result.exists()) {
            return result;
        }

        try (FileOutputStream fos = new FileOutputStream(result); XWPFDocument doc = new XWPFDocument()) {
            CTDocument1 document = doc.getDocument();
            CTBody body = document.getBody();
            if (!body.isSetSectPr()) {
                body.addNewSectPr();
            }
            CTSectPr section = body.getSectPr();
            if (!section.isSetPgSz()) {
                section.addNewPgSz();
            }
            CTPageSz pageSize = section.getPgSz();
            pageSize.setOrient(STPageOrientation.LANDSCAPE);
            pageSize.setW(BigInteger.valueOf(15840));

            int i = 1;
            for (File question : files) {
                Util.appendImg(doc, FileUtils.getFile(JOIN_EXAM_QUESTION_IMAGE_DIR, (i++) + ".png"));
                JPoi.append(doc, question);
            }

//            Util.appendImg(doc, FileUtils.getFile(JOIN_EXAM_QUESTION_IMAGE_DIR, "答案.png"));
            doc.write(fos);
            return Util.docxToDoc(result);
        }
    }

}

package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jzb.execution.domain.repository.ExamQuestionLabRepository;
import org.jzb.execution.domain.repository.ExamQuestionRepository;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.concurrent.Future;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@Path("examQuestionLabManage")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class ExamQuestionLabManageResource {
    @Resource
    private ManagedScheduledExecutorService ses;
    @Inject
    private ExamQuestionLabRepository examQuestionLabRepository;
    @Inject
    private ExamQuestionRepository examQuestionRepository;

    @GET
    public Response current(@Context SecurityContext sc,
                            @QueryParam("examQuestionLabId") String examQuestionLabId) throws Exception {
        Principal principal = sc.getUserPrincipal();
        Future<ArrayNode> examQuestionLabsFuture = ses.submit(() -> queryExamQuestionLab(principal));
        Future<ArrayNode> examQuestionsFuture = ses.submit(() -> queryExamQuestions(principal, examQuestionLabId));
        ObjectNode result = MAPPER.createObjectNode();
        result.set("examQuestionLabs", examQuestionLabsFuture.get());
        result.set("examQuestions", examQuestionsFuture.get());
        return Response.ok(result).build();
    }

    private ArrayNode queryExamQuestionLab(Principal principal) {
        ArrayNode result = MAPPER.createArrayNode();
        examQuestionLabRepository.query(principal)
                .map(lab -> {
                    ObjectNode node = MAPPER.convertValue(lab, ObjectNode.class);
                    node.put("examQuestionCount", examQuestionRepository.countBy(lab));
                    return node;
                })
                .forEach(result::add);
        return result;
    }

    private ArrayNode queryExamQuestions(Principal principal, String examQuestionLabId) {
        ArrayNode result = MAPPER.createArrayNode();
        examQuestionRepository.queryByLabId(principal, examQuestionLabId)
                .stream()
                .map(MAPPER.getNodeFactory()::pojoNode)
                .forEach(result::add);
        return result;
    }

}

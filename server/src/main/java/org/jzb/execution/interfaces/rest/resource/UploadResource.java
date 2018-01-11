package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.NotBlank;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jzb.execution.Util;
import org.jzb.execution.application.UploadService;
import org.jzb.execution.application.command.SimpleStringDTO;
import org.jzb.execution.domain.UploadFile;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.UploadFileRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.*;
import static org.jzb.Constant.MAPPER;
import static org.jzb.execution.Constant.ROOT_URL;

/**
 * Created by jzb on 17-4-15.
 */
@Stateless
@Path("uploads")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UploadResource {
    @Inject
    private UploadFileRepository uploadFileRepository;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private UploadService uploadService;

    @Path("{id}/downloadToken")
    @PUT
    public JsonNode downloadToken(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        UploadFile uploadFile = uploadFileRepository.find(id);
        String token = Util.downloadToken(sc.getUserPrincipal(), uploadFile._file(), uploadFile.getMediaType(), uploadFile.getFileName());
        return MAPPER.createObjectNode().put("token", token);
    }

    //文件上传，文件名乱码处理
    @Path("{id}/fileName")
    @PUT
    public UploadFile updateFileName(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull SimpleStringDTO command) throws Exception {
        Validate.notBlank(command.getData());
        return uploadService.updateFileName(sc.getUserPrincipal(), id, command);
    }

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    public UploadFile post(@Context SecurityContext sc, MultipartFormDataInput input) throws Exception {
        return uploadService.save(sc.getUserPrincipal(), input);
    }

    @Path("editors")
    @POST
    @Consumes(MULTIPART_FORM_DATA)
    public JsonNode editors(@Context SecurityContext sc, MultipartFormDataInput input) throws Exception {
        UploadFile uploadFile = uploadService.save(sc.getUserPrincipal(), input);
        String token = Util.editorToken(sc.getUserPrincipal(), uploadFile._file(), uploadFile.getMediaType(), uploadFile.getFileName());
        return MAPPER.createObjectNode().put("link", ROOT_URL + "/api/opens/downloads/" + token + "/image/w/1208");
    }

    @Path("editors2world")
    @POST
    @Consumes(MULTIPART_FORM_DATA)
    public JsonNode image2world(@Context SecurityContext sc, MultipartFormDataInput input) throws Exception {
        final UploadFile uploadFile = uploadService.image2word(sc.getUserPrincipal(), input);
        final ObjectNode node = MAPPER.convertValue(uploadFile, ObjectNode.class);
        String token = Util.editorToken(sc.getUserPrincipal(), uploadFile._file(), uploadFile.getMediaType(), uploadFile.getFileName());
        return node.put("link", ROOT_URL + "/api/opens/downloads/" + token + "/image/w/1208");
    }

    @Path("base64Image2world")
    @POST
    @Consumes(TEXT_PLAIN)
    public JsonNode base64Image2world(@Context SecurityContext sc, String s) throws Exception {
        final UploadFile uploadFile = uploadService.base64Image2world(sc.getUserPrincipal(), s);
        final ObjectNode node = MAPPER.convertValue(uploadFile, ObjectNode.class);
        String token = Util.editorToken(sc.getUserPrincipal(), uploadFile._file(), uploadFile.getMediaType(), uploadFile.getFileName());
        return node.put("link", ROOT_URL + "/api/opens/downloads/" + token + "/image/w/1208");
    }

}


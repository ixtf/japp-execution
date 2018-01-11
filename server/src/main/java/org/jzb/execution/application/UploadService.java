package org.jzb.execution.application;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jzb.execution.application.command.SimpleStringDTO;
import org.jzb.execution.domain.UploadFile;

import java.security.Principal;

/**
 * Created by jzb on 17-4-15.
 */
public interface UploadService {

    UploadFile save(Principal principal, MultipartFormDataInput input) throws Exception;

    UploadFile updateFileName(Principal principal, String id, SimpleStringDTO command) throws Exception;

    UploadFile image2word(Principal principal, MultipartFormDataInput mfdi) throws Exception;

    UploadFile base64Image2world(Principal principal, String s) throws Exception;
}

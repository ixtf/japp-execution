package org.jzb.execution.domain.repository;

import org.jzb.execution.domain.UploadFile;

import java.io.File;

/**
 * Created by jzb on 17-4-15.
 */

public interface UploadFileRepository {

    UploadFile save(UploadFile uploadFile, File tmpFile);

    UploadFile updateFileName(String id, String fileName);

    UploadFile find(String id);

    UploadFile findBySha256Hex(String sha256Hex);
}

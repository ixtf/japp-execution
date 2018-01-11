package org.jzb.execution.infrastructure.persistence.jpa;

import org.apache.commons.io.FileUtils;
import org.jzb.execution.domain.UploadFile;
import org.jzb.execution.domain.repository.UploadFileRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.IOException;

/**
 * Created by jzb on 17-2-27.
 */
@ApplicationScoped
public class JpaUploadFileRepository extends JpaCURDRepository<UploadFile, String> implements UploadFileRepository {
    @Override
    public UploadFile findBySha256Hex(String sha256Hex) {
        TypedQuery<UploadFile> query = em.createNamedQuery("UploadFile.findBySha256Hex", UploadFile.class)
                .setParameter("sha256Hex", sha256Hex);
        return querySingle(query);
    }

    @Override
    public UploadFile save(UploadFile entity, File tmpFile) {
        UploadFile uploadFile = super.save(entity);
        File file = uploadFile._file();
        if (!file.exists()) {
            try {
                FileUtils.moveFile(tmpFile, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return uploadFile;
    }

    @Override
    public UploadFile updateFileName(String id, String fileName) {
        UploadFile uploadFile = find(id);
        uploadFile.setFileName(fileName);
        return save(uploadFile);
    }
}

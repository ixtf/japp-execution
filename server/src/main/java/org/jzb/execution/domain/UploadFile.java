package org.jzb.execution.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sun.security.auth.UserPrincipal;
import org.apache.commons.io.FileUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.J;
import org.jzb.execution.Util;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;

import static org.jzb.execution.Constant.UPLOAD_BASE_PATH;

/**
 * Created by jzb on 17-4-15.
 */
@Entity
@Table(name = "T_UPLOADFILE")
@NamedQueries({
        @NamedQuery(name = "UploadFile.findBySha256Hex", query = "SELECT o FROM UploadFile o WHERE o.sha256Hex=:sha256Hex")
})
public class UploadFile extends AbstractLogable {
    @Column(unique = true)
    @NotBlank
    private String sha256Hex;
    @NotBlank
    private String fileName;
    private long fileSize;
    private String mediaType;

    public File _file() {
        LocalDate ld = J.localDate(getCreateDateTime());
        String year = String.valueOf(ld.getYear());
        String month = String.valueOf(ld.getMonthValue());
        String day = String.valueOf(ld.getDayOfMonth());
        return FileUtils.getFile(UPLOAD_BASE_PATH, year, month, day, id);
    }

    @JsonGetter
    public String getDownloadToken() {
        return Util.downloadToken(new UserPrincipal(getCreator().getName()), _file(), getMediaType(), getFileName());
    }

    public String getSha256Hex() {
        return sha256Hex;
    }

    public void setSha256Hex(String sha256Hex) {
        this.sha256Hex = sha256Hex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}

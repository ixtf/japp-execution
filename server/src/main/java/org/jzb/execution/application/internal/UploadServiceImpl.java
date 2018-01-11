package org.jzb.execution.application.internal;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jzb.J;
import org.jzb.execution.application.UploadService;
import org.jzb.execution.application.command.SimpleStringDTO;
import org.jzb.execution.domain.UploadFile;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.UploadFileRepository;
import org.jzb.img.JImg;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jzb.execution.Constant.UPLOAD_TMP_PATH;

/**
 * Created by jzb on 17-4-15.
 */

@Stateless
public class UploadServiceImpl implements UploadService {
    private static final Pattern p = Pattern.compile("filename=\"(.+)\"");
    @Inject
    private UploadFileRepository uploadFileRepository;
    @Inject
    private OperatorRepository operatorRepository;

    private static File getWord(File imgFile) throws IOException, InvalidFormatException {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph p = doc.createParagraph();
        XWPFRun r = p.createRun();

        int format;
        String contentType = Files.probeContentType(imgFile.toPath());
        if (contentType.endsWith("emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
        else if (contentType.endsWith("wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
        else if (contentType.endsWith("pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
        else if (contentType.endsWith("jpeg") || contentType.endsWith("jpg")) format = XWPFDocument.PICTURE_TYPE_JPEG;
        else if (contentType.endsWith("png")) format = XWPFDocument.PICTURE_TYPE_PNG;
        else if (contentType.endsWith("dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
        else if (contentType.endsWith("gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
        else if (contentType.endsWith("tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
        else if (contentType.endsWith("eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
        else if (contentType.endsWith("bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
        else if (contentType.endsWith("wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
        else throw new RuntimeException();

        ImageIcon ii = new ImageIcon(imgFile.getCanonicalPath());
        Image image = ii.getImage();
        int iWidth = image.getWidth(null);
        int iHeight = image.getHeight(null);
        final int maxWidth = 500;
        if (iWidth > maxWidth) {
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

            int oldWidth = iWidth;
            iWidth = maxWidth;
            iHeight = iHeight * maxWidth / oldWidth;
        }
        r.addPicture(new FileInputStream(imgFile), format, imgFile.getName(), Units.toEMU(iWidth), Units.toEMU(iHeight));

        File wordFile = FileUtils.getFile(UPLOAD_TMP_PATH, J.uuid58());
        FileOutputStream out = new FileOutputStream(wordFile);
        doc.write(out);
        out.close();
        doc.close();
        return wordFile;
    }

    @Override
    public UploadFile updateFileName(Principal principal, String id, SimpleStringDTO dto) {
        return uploadFileRepository.updateFileName(id, dto.getData());
    }

    /**
     * 把上传的文件直接保存值数据库
     * 相当于永久保存了
     * 目前的处理是，只要文件没上传过，就保存数据库
     * 上传过的文件，不会在数据库生成记录
     */
    //TODO 考虑并发上传的问题，同一个文件，考虑用数据库表的锁定，锁定uploadfile主键那一行数据
    @Override
    public UploadFile save(Principal principal, MultipartFormDataInput mfdi) throws IOException {
        Map<String, List<InputPart>> uploadForm = mfdi.getFormDataMap();
        if (MapUtils.isEmpty(uploadForm))
            throw new RuntimeException();
        List<InputPart> inputParts = uploadForm.get("file");
        if (J.isEmpty(inputParts)) {
            throw new RuntimeException();
        }
        InputPart inputPart = inputParts.get(0);
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        File tmpFile = FileUtils.getFile(UPLOAD_TMP_PATH, J.uuid58());
        FileUtils.copyInputStreamToFile(inputStream, tmpFile);
        String sha256Hex = DigestUtils.sha256Hex(new FileInputStream(tmpFile));
        UploadFile uploadFile = uploadFileRepository.findBySha256Hex(sha256Hex);
        if (uploadFile == null) {
            uploadFile = new UploadFile();
            uploadFile.setSha256Hex(sha256Hex);
            uploadFile.setFileSize(tmpFile.length());
            uploadFile.setFileName(getFileName(mfdi));
            uploadFile.setMediaType(inputPart.getMediaType().toString());
            final Operator operator = operatorRepository.find(principal);
            uploadFile._loginfo(operator);
            uploadFile = uploadFileRepository.save(uploadFile, tmpFile);
        }
        return uploadFile;
    }

    @Override
    public UploadFile image2word(Principal principal, MultipartFormDataInput mfdi) throws IOException, InvalidFormatException {
        Map<String, List<InputPart>> uploadForm = mfdi.getFormDataMap();
        if (MapUtils.isEmpty(uploadForm))
            throw new RuntimeException();
        List<InputPart> inputParts = uploadForm.get("file");
        if (CollectionUtils.isEmpty(inputParts)) {
            throw new RuntimeException();
        }
        InputPart inputPart = inputParts.get(0);
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        File file = FileUtils.getFile(UPLOAD_TMP_PATH, J.uuid58());
        FileUtils.copyInputStreamToFile(inputStream, file);
        return saveImage2world(principal, file);
    }

    @Override
    public UploadFile base64Image2world(Principal principal, String s) throws Exception {
        final BufferedImage bufferedImage = JImg.base64(s);
        File file = FileUtils.getFile(UPLOAD_TMP_PATH, J.uuid58());
        ImageIO.write(bufferedImage, "png", file);
        return saveImage2world(principal, file);
    }

    private UploadFile saveImage2world(Principal principal, File imgFile) throws IOException, InvalidFormatException {
        File wordFile = getWord(imgFile);
        String sha256Hex = DigestUtils.sha256Hex(new FileInputStream(wordFile));
        UploadFile uploadFile = uploadFileRepository.findBySha256Hex(sha256Hex);
        if (uploadFile == null) {
            uploadFile = new UploadFile();
            uploadFile.setSha256Hex(sha256Hex);
            uploadFile.setFileSize(wordFile.length());
            uploadFile.setFileName(wordFile.getName() + ".docx");
            uploadFile.setMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            final Operator operator = operatorRepository.find(principal);
            uploadFile._loginfo(operator);
            uploadFile = uploadFileRepository.save(uploadFile, wordFile);
        }
        return uploadFile;
    }

    private String getFileName(MultipartFormDataInput input) {
        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            if (uploadForm.get("fileName") != null) {
                String fileName = uploadForm.get("fileName").get(0).getBodyAsString();
                return URLDecoder.decode(fileName, StandardCharsets.UTF_8.name());
            }
            String disposition = uploadForm.get("file").get(0).getHeaders().get("Content-Disposition").get(0);
            Matcher m = p.matcher(disposition);
            if (m.find())
                return m.group(1);
        } catch (Exception e) {
        }
        return "__no__";
    }
}

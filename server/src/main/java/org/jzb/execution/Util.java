/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jzb.execution;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jzb.J;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.Task;
import org.jzb.execution.domain.TaskGroup;
import org.jzb.execution.domain.UploadFile;
import org.jzb.execution.domain.extra.ExamQuestion;
import org.jzb.img.JImg;
import org.jzb.poi.JPoi;
import org.jzb.weixin.mp.MpClient;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.ws.rs.core.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jzb.execution.Constant.*;
import static org.zwobble.mammoth.internal.util.Base64Encoding.streamToBase64;

/**
 * @author jzb
 */
public class Util {
    public static Jws<Claims> jws(String token) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(JAPP_EXECUTION_PERMANENT_KEY).parseClaimsJws(token);
        if (!ROOT_URL.equals(jws.getBody().getSubject())) {
            throw new RuntimeException();
        }
        return jws;
    }

    public static String downloadToken(Principal principal, File file, String mediaType, String downloadName) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS512, JAPP_EXECUTION_PERMANENT_KEY)
                .setExpiration(DateUtils.addDays(new Date(), 1))
                .claim("filePath", file.getAbsolutePath())
                .claim("mediaType", mediaType)
                .claim("downloadName", downloadName)
                .setSubject(ROOT_URL)
                .setIssuedAt(new Date())
                .setIssuer(principal.getName())
                .setAudience(principal.getName())
                .compact();
    }

    public static String editorToken(Principal principal, File file, String mediaType, String downloadName) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS512, JAPP_EXECUTION_PERMANENT_KEY)
                .claim("filePath", file.getAbsolutePath())
                .claim("mediaType", mediaType)
                .claim("downloadName", downloadName)
                .setSubject(ROOT_URL)
                .setIssuedAt(new Date())
                .setIssuer(principal.getName())
                .setAudience(principal.getName())
                .compact();
    }

    public static Response downloadResponseByDownloadToken(String token, Request request) throws Exception {
        Claims claims = jws(token).getBody();
        String filePath = claims.get("filePath", String.class);
        String downloadName = claims.get("downloadName", String.class);
        String encodeDownloadName = J.urlEncode(downloadName);

        EntityTag etag = new EntityTag(J.uuid58(filePath));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            StreamingOutput result = (out) -> Files.copy(FileUtils.getFile(filePath), out);
            builder = Response.ok(result);
        }
        return cc(builder, etag, claims.getExpiration()).header("Content-Disposition", "attachment;filename=" + encodeDownloadName).build();
    }

    private static Response.ResponseBuilder cc(Response.ResponseBuilder builder, EntityTag etag, Date expiration) {
        CacheControl cc = new CacheControl();
        cc.setPrivate(true);
        long maxAge = Integer.MAX_VALUE;
        if (expiration != null) {
            maxAge = ChronoUnit.SECONDS.between(LocalDateTime.now(), J.localDateTime(expiration));
        }
        cc.setMaxAge((int) maxAge);
        return builder.tag(etag).cacheControl(cc);
    }

    public static Response commonResponseByDownloadToken(String token, Request request) throws Exception {
        Claims claims = jws(token).getBody();
        String mediaType = claims.get("mediaType", String.class);
        String filePath = claims.get("filePath", String.class);

        EntityTag etag = new EntityTag(J.uuid58(filePath));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            builder = Response.ok(FileUtils.getFile(filePath), mediaType);
        }
        return cc(builder, etag, claims.getExpiration()).build();
    }

    public static Response imageResponseByDownloadToken(String token, Request request, int width) throws Exception {
        Claims claims = jws(token).getBody();
        String mediaType = claims.get("mediaType", String.class);
        String filePath = claims.get("filePath", String.class);
        String downloadName = claims.get("downloadName", String.class);
        String formatName = FilenameUtils.getExtension(downloadName);

        EntityTag etag = new EntityTag(J.uuid58(filePath, "" + width));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            File file = FileUtils.getFile(filePath);
            BufferedImage image = JImg.resize(file, width, 1);
            StreamingOutput result = (out) -> ImageIO.write(image, StringUtils.defaultIfBlank(formatName, "jpg"), out);
            builder = Response.ok(result, mediaType);
        }
        return cc(builder, etag, claims.getExpiration()).build();
    }

    public static Response pdfResponseByDownloadToken(String token, Request request) throws Exception {
        Claims claims = jws(token).getBody();
        String filePath = claims.get("filePath", String.class);

        EntityTag etag = new EntityTag(J.uuid58(filePath, "pdf"));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            File file = FileUtils.getFile(filePath);
            File pdfFile = FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("pdf") ? file : Util.toPdf(file);
            builder = Response.ok(pdfFile);
        }
        return cc(builder, etag, claims.getExpiration()).build();
    }

    public static String shareToken(Principal principal, Task task) {
        return Jwts.builder().signWith(SignatureAlgorithm.HS512, JAPP_EXECUTION_PERMANENT_KEY)
                .claim("taskId", task.getId())
                .setSubject(ROOT_URL)
                .setIssuedAt(new Date())
                .setIssuer(principal.getName())
                .setAudience(principal.getName())
                .compact();
    }

    public static String shareToken(Principal principal, TaskGroup taskGroup, Collection<Task> tasks) {
        Collection<String> taskIds = tasks.stream()
                .parallel()
                .map(Task::getId)
                .collect(Collectors.toSet());
        return Jwts.builder().signWith(SignatureAlgorithm.HS512, JAPP_EXECUTION_PERMANENT_KEY)
                .claim("taskGroupId", taskGroup.getId())
                .claim("taskIds", taskIds)
                .setSubject(ROOT_URL)
                .setIssuedAt(new Date())
                .setIssuer(principal.getName())
                .setAudience(principal.getName())
                .compact();
    }

    public static Response toDownloadResponse(UploadFile uploadFile) throws Exception {
        return toDownloadResponse(uploadFile._file(), uploadFile.getFileName());
    }

    public static Response toDownloadResponse(File file, String fileName) throws Exception {
        StreamingOutput result = (out) -> Files.copy(file, out);
        fileName = J.urlEncode(fileName);
        return Response.ok(result).header("Content-Disposition", "attachment;filename=" + fileName).build();
    }

    public static Response toHtmlResponse(UploadFile uploadFile) throws Exception {
        File dir = FileUtils.getFile(UPLOAD_TMP_PATH, uploadFile.getId() + "html");
        FileUtils.forceMkdir(dir);
        File htmlFile = FileUtils.getFile(dir, "index.html");
        if (!htmlFile.exists()) {
            final String s = toHtml(uploadFile._file());
            FileUtils.write(htmlFile, s);
        }
        StreamingOutput result = (out) -> Files.copy(htmlFile, out);
        return Response.ok(result).build();
    }

    public static String toHtml(File file) throws IOException {
        DocumentConverter converter = new DocumentConverter()
                .addStyleMap("b => em")
                .addStyleMap("i => strong")
                .addStyleMap("u => em")
                .addStyleMap("strike => del")
                .addStyleMap("comment-reference => sup")
                .imageConverter(image -> {
                    String base64 = streamToBase64(image::getInputStream);
                    String src = "data:" + image.getContentType() + ";base64," + base64;
                    Map<String, String> attributes = Maps.newHashMap();
                    attributes.put("src", src);
                    attributes.put("style", "max-width: 500px;");
                    return attributes;
                });
        Result<String> result = converter.convertToHtml(file);
        return result.getValue();
    }

    public static File toPdf(File file) throws Exception {
        File pdfFile = FileUtils.getFile(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".pdf");
        if (pdfFile.exists()) {
            return pdfFile;
        }

        String tpl = "/opt/libreoffice5.3/program/soffice --convert-to pdf --outdir ${outdir} ${inPath}";
        String command = J.strTpl(tpl, ImmutableMap.of("outdir", file.getParentFile().getAbsolutePath(), "inPath", file.getAbsolutePath()));
        J.exeCli(command);
        return FileUtils.getFile(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".pdf");
    }

    public static File docxToDoc(File file) {
        File docFile = FileUtils.getFile(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".doc");
        if (docFile.exists()) {
            return docFile;
        }

        String tpl = "/opt/libreoffice5.3/program/soffice --convert-to doc --outdir ${outdir} ${inPath}";
        String command = J.strTpl(tpl, ImmutableMap.of("outdir", file.getParentFile().getAbsolutePath(), "inPath", file.getAbsolutePath()));
        J.exeCli(command);
        return FileUtils.getFile(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".doc");
    }

    public static String getPlanShareDetailWxUrl(Plan plan, MpClient client) {
        String nextUrl = ROOT_URL + "/planShareDetail?planId=" + plan.getId();
        return authorizeUrl(nextUrl, client);
    }

    public static String authorizeUrl(String next, MpClient client) {
        next = J.urlEncode(next);
        String url = ROOT_URL + "/oauth/weixin?next=" + next;
        return client.authorizeUrl(url, "123");
    }

    public static File joinExamQuestions(Stream<ExamQuestion> examQuestionStream) throws Exception {
        Collection<File> questions = Lists.newArrayList();
        Collection<File> answers = Lists.newArrayList();
        examQuestionStream.forEach(examQuestion -> {
            UploadFile question = examQuestion.getQuestion();
            questions.add(question._file());
            UploadFile answer = examQuestion.getAnswer();
            answers.add(question._file());
            answers.add(answer._file());
        });
        String[] uploadFilePaths = Stream.concat(questions.stream(), answers.stream())
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
            for (File question : questions) {
                appendImg(doc, FileUtils.getFile(JOIN_EXAM_QUESTION_IMAGE_DIR, (i++) + ".png"));
                JPoi.append(doc, question);
            }

            appendImg(doc, FileUtils.getFile(JOIN_EXAM_QUESTION_IMAGE_DIR, "答案.png"));

            i = 1;
            // 题目和答案，需要两张图片后，出现第几题
            int j = 0;
            for (File answer : answers) {
                if (j % 2 == 0) {
                    appendImg(doc, FileUtils.getFile(JOIN_EXAM_QUESTION_IMAGE_DIR, (i++) + ".png"));
                }
                j++;
                JPoi.append(doc, answer);
            }
            doc.write(fos);
//            return result;
            return Util.docxToDoc(result);
        }
    }

    public static void appendImg(XWPFDocument doc, File imgFile) throws IOException, InvalidFormatException {
        if (imgFile.exists()) {
            ImageIcon ii = new ImageIcon(imgFile.getCanonicalPath());
            Image image = ii.getImage();
            int iWidth = image.getWidth(null);
            int iHeight = image.getHeight(null);
            XWPFParagraph p = doc.createParagraph();
            XWPFRun r = p.createRun();
            r.addPicture(new FileInputStream(imgFile), XWPFDocument.PICTURE_TYPE_PNG, imgFile.getName(), Units.toEMU(iWidth), Units.toEMU(iHeight));
        }
    }

}

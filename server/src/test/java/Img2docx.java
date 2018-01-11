import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.util.Date;

/**
 * 描述：
 *
 * @author jzb 2017-11-13
 */
public class Img2docx {
    static File imgFile = FileUtils.getFile("/home/jzb/桌面/test.png");
    static File wordFile = FileUtils.getFile("/home/jzb/桌面/test.docx");
    static int maxWidth = 600;

    public static void main(String[] args) throws Exception {
        // 20171116071104
        final Date date = DateUtils.parseDate("20171116071104", "yyyyMMddHHmmss");
        System.out.println(date);

        // XWPFDocument doc = new XWPFDocument();
        // XWPFParagraph p = doc.createParagraph();
        // XWPFRun r = p.createRun();
        //
        // int format;
        // String contentType = Files.probeContentType(imgFile.toPath());
        // if (contentType.endsWith("emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
        // else if (contentType.endsWith("wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
        // else if (contentType.endsWith("pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
        // else if (contentType.endsWith("jpeg") || contentType.endsWith("jpg")) format = XWPFDocument.PICTURE_TYPE_JPEG;
        // else if (contentType.endsWith("png")) format = XWPFDocument.PICTURE_TYPE_PNG;
        // else if (contentType.endsWith("dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
        // else if (contentType.endsWith("gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
        // else if (contentType.endsWith("tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
        // else if (contentType.endsWith("eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
        // else if (contentType.endsWith("bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
        // else if (contentType.endsWith("wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
        // else throw new RuntimeException();
        //
        // ImageIcon ii = new ImageIcon(imgFile.getCanonicalPath());
        // Image image = ii.getImage();
        // int iWidth = image.getWidth(null);
        // int iHeight = image.getHeight(null);
        // if (iWidth > maxWidth) {
        //     CTDocument1 document = doc.getDocument();
        //     CTBody body = document.getBody();
        //     if (!body.isSetSectPr()) {
        //         body.addNewSectPr();
        //     }
        //     CTSectPr section = body.getSectPr();
        //     if(!section.isSetPgSz()) {
        //         section.addNewPgSz();
        //     }
        //     CTPageSz pageSize = section.getPgSz();
        //     pageSize.setOrient(STPageOrientation.LANDSCAPE);
        //     pageSize.setW(BigInteger.valueOf(15840));
        //
        //     int oldWidth = iWidth;
        //     iWidth = maxWidth;
        //     iHeight = iHeight * maxWidth / oldWidth;
        // }
        // r.addPicture(new FileInputStream(imgFile), format, imgFile.getName(), Units.toEMU(iWidth), Units.toEMU(iHeight));
        // FileOutputStream out = new FileOutputStream(wordFile);
        // doc.write(out);
        // out.close();
        // doc.close();
    }
}

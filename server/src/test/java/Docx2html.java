import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.zwobble.mammoth.internal.util.Base64Encoding.streamToBase64;

/**
 * 描述：
 *
 * @author jzb 2017-11-14
 */
public class Docx2html {
    public static void main(String[] args) throws IOException {
        File file = FileUtils.getFile("/home/jzb/桌面/test.docx");
        DocumentConverter converter = new DocumentConverter()
                .imageConverter(image -> {
                    String base64 = streamToBase64(image::getInputStream);
                    String src = "data:" + image.getContentType() + ";base64," + base64;
                    Map<String, String> attributes = Maps.newHashMap();
                    attributes.put("src", src);
                    attributes.put("style", "max-width: 500px;");
                    return attributes;
                });
        Result<String> result = converter.convertToHtml(file);
        System.out.println(result.getValue());
    }
}

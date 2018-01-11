import com.google.common.collect.ImmutableMap;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jzb.J;
import org.jzb.execution.Util;
import org.jzb.social.core.AbstractExpireToken;
import org.jzb.weixin.mp.MpClient;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import static org.jzb.execution.Constant.MP_WEIXIN_CONFIG_PATH;

/**
 * 描述：
 *
 * @author jzb 2017-11-21
 */
public class KfMsgTest {
    static MpClient mpClient;

    static {
        try {
            Properties p = new Properties();
            File file = new File(MP_WEIXIN_CONFIG_PATH);
            p.load(new FileReader(file));
            mpClient = MpClient.getInstance(p, new AbstractExpireToken<String>() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://www.wjh001.com/execution/api/auth/weixin_mp_access_token?u=admin&p=tomking")
                        .get()
                        .build();

                @Override
                public String get() throws Exception {
                    return client.newCall(request).execute().body().string();
                }

                @Override
                protected void fetch() throws Exception {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        final String urlTpl = "http://www.wjh001.com/execution" + "/enlists/progress?enlistId=${enlistId}";
        final String next = J.strTpl(urlTpl, ImmutableMap.of("enlistId", "SnRQFCcwDKtXGXDZ6bVh68"));
        final String url = Util.authorizeUrl(next, mpClient);
        String content = "商户：" + "算学宫" + "\n名称：" + "培训" + "\n<a href=\"" + url + "\">点击查看和报名</a>";
        mpClient.msgKf("oohBQwjH5QPqyNVFzf54MTMSBV0Y").text().content(content).call();
    }
}

import com.google.common.collect.ImmutableMap;
import org.jzb.J;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.jzb.weixin.mp.util.MpConstant.AUTHORIZE_URL_TPL;

/**
 * 描述：
 *
 * @author jzb 2017-11-18
 */
public class WeixinMenu {
    public static void main(String[] args) {
        System.out.println(authorizeUrl("http://www.wjh001.com/execution/tasks/progress"));
        System.out.println(authorizeUrl("http://www.wjh001.com/execution/personal"));
    }

    private static String authorizeUrl(String next) {
        DateFormat df = new SimpleDateFormat("yyyy 年 MM 月 dd 日 HH:mm:ss");
        System.out.println(df.format(new Date()));
        next = J.urlEncode(next);
        String redirect_uri = "http://www.wjh001.com/execution/oauth/weixin?next=" + next;
        redirect_uri = J.urlEncode(redirect_uri);
        final ImmutableMap<String, String> map = ImmutableMap.of("appid", "wx3902a712d038c298", "redirect_uri", redirect_uri, "state", "wjh");
        return J.strTpl(AUTHORIZE_URL_TPL, map);
    }
}

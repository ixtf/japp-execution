package org.jzb.execution;

import io.jsonwebtoken.impl.crypto.MacProvider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jzb.social.core.AbstractExpireToken;
import org.jzb.weinxin.open.OpenClient;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.pay.sl.PaySlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.jzb.execution.Constant.*;

/**
 * @author jzb
 */
public class Resource {
    @Produces
    @PersistenceContext
    private EntityManager em;

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
    }

    @Produces
    @Singleton
    public MpClient produceMpClient() throws IOException {
        Properties p = new Properties();
        File file = new File(MP_WEIXIN_CONFIG_PATH);
        p.load(new FileReader(file));
        return MpClient.getInstance(p);
        // return MpClient.getInstance(p, testAccessTokenTest());
    }

    private AbstractExpireToken<String> testAccessTokenTest() throws IOException {
        return new AbstractExpireToken<String>() {
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
        };
    }

    @Produces
    @Singleton
    public OpenClient produceOpenClient() throws IOException {
        Properties p = new Properties();
        File file = new File(OPEN_WEIXIN_CONFIG_PATH);
        p.load(new FileReader(file));
        return OpenClient.getInstance(p);
    }

    @Produces
    @Singleton
    public PaySlClient producePayClient() throws IOException {
        Properties p = new Properties();
        File file = new File(PAY_SL_WEIXIN_CONFIG_PATH);
        p.load(new FileReader(file));
        return PaySlClient.getInstance(p);
    }

    @Produces
    @Singleton
    public SecretKey produceSecretKey() {
        return MacProvider.generateKey();
    }

}

package org.jzb.execution;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;
import org.jzb.execution.domain.TaskGroup;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

/**
 * @author jzb
 */
public class Constant {
    public static final Properties CONFIG;
    public static final TaskGroup MY_TASK_GROUP;

    static {
        MY_TASK_GROUP = new TaskGroup();
        MY_TASK_GROUP.setId("0");
        MY_TASK_GROUP.setName("我的");
        try {
            CONFIG = new Properties();
            CONFIG.load(new FileReader(new File(System.getProperty("EXECUTION_WJH_CONFIG", "/home/jzb/data/japp-execution-wjh/config.properties"))));
            File tmpDir = FileUtils.getFile(System.getProperty("java.io.tmpdir"), "japp-execution");
            FileUtils.forceMkdir(tmpDir);
            UPLOAD_TMP_PATH = tmpDir.getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String UPLOAD_TMP_PATH;
    public static final Collection<String> ADMIN_IDS = ImmutableSet.of("Jmg62HxQV2mzUGM8Qqnq1U", "N8nKbXJtvBfzarKt3piZ34");
    public static final String ROOT_URL = CONFIG.getProperty("ROOT_URL", "http://www.wjh001.com/execution");
    public static final String AD_URL = CONFIG.getProperty("AD_URL", "http://www.wjh001.com");
    public static final String LUCENE_BASE_PATH = CONFIG.getProperty("LUCENE_BASE_PATH");
    public static final String UPLOAD_BASE_PATH = CONFIG.getProperty("UPLOAD_BASE_PATH");
    public static final String MP_WEIXIN_CONFIG_PATH = CONFIG.getProperty("MP_WEIXIN_CONFIG_PATH");
    public static final String OPEN_WEIXIN_CONFIG_PATH = CONFIG.getProperty("OPEN_WEIXIN_CONFIG_PATH");
    public static final String PAY_SL_WEIXIN_CONFIG_PATH = CONFIG.getProperty("PAY_SL_WEIXIN_CONFIG_PATH");
    // 错题生成序列图片
    public static final String JOIN_EXAM_QUESTION_IMAGE_DIR = CONFIG.getProperty("JOIN_EXAM_QUESTION_IMAGE_DIR");
    //服务器 jwt token 永久的加密key uuidbase(japp-execution)
    public static final String JAPP_EXECUTION_PERMANENT_KEY = "60665c75-6a5f-37ab-b64c-a9bd52a6a518";

    public static final class ErrorCode {
        public static final String SYSTEM = "E00000";
        //未登入
        public static final String NO_AUTHENTICATION = "E00001";
        //权限不足
        public static final String NO_AUTHORIZATION = "E00002";
    }

    public static final class SmsTemplate {

        public static final String ST1 = "主题：{0}；任务：{1}；提醒：{2}；处理网址：{3}";
        public static final String ST2 = "主题：{0}；任务：{1}；执行情况：{2}；执行人：{3}[{4}]";
        public static final String ST3 = "主题：{0}；任务：{1}；执行情况：{2}；备注消息：{5}；执行人：{3}[{4}]";
        public static final String ST4 = "主题：{0}；任务：{1}；执行情况：{2}；执行人：{3}";
        public static final String ST5 = "主题：个人提醒；任务：；提醒：{0}；处理网址：";
    }
}

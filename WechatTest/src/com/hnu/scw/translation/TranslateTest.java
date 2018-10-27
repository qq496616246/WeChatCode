package com.hnu.scw.translation;

import org.junit.Test;
import java.io.UnsupportedEncodingException;

/**
 * @author scw
 * @create 2018-01-17 19:29
 * @desc 测试百度翻译功能是否可行
 **/
public class TranslateTest {

    /**
     * 测试翻译的结果
     */
    @Test
    public void translateTest() throws UnsupportedEncodingException {
        String transResult = TranslationUtils.translate("王耀东\n足球");
        System.out.println(transResult);

        /*//百度翻译API服务的秘钥（自己的秘钥）
        String APP_ID = "20180117000116254";
        String SECURITY_KEY = "NcEg3feTzYQN_S2Hnteb";
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String query = "高度600米";
        System.out.println(api.getTransResult(query, "auto", "en"));*/
    }
}

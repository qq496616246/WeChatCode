package com.hnu.scw.translation;

import com.hnu.scw.utils.WeiXinUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author scw
 * @create 2018-01-17 19:17
 * @desc 进行百度翻译功能的开发
 **/
public class TranslationUtils {

    private static final String TRANLATE_APP_ID = "20180117000116254";
    private static final String SECURITY_KEY = "NcEg3feTzYQN_S2Hnteb";
    /**
     * 利用百度翻译的接口url，进行翻译功能
     * @param source  需要进行翻译的内容
     * @return  返回多种语言的翻译结果（中文，英式英语，美式英语）
     */
    public static String translate(String source) throws UnsupportedEncodingException {
            //这个自己在百度开发服务中心找的，还有很多其他的API的接口
            String url = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=KEYWORD&from=auto&to=en&appid=APPID&salt=SALT&sign=SIGN";
            //一定要将需要进行翻译的内容转为utf-8，因为对于中文是无法进行http传输的
            url = url.replace("KEYWORD", URLEncoder.encode(source,"UTF-8"));
            url = url.replace("APPID", TRANLATE_APP_ID);
            //随机数
            String salt = String.valueOf(System.currentTimeMillis());
            url = url.replace("SALT", salt);
            // 加密前的原文
            String sign = TRANLATE_APP_ID + source + salt +SECURITY_KEY;
            url = url.replace("SIGN", MD5.md5(sign));
            //返回结果为json格式
            JSONObject jsonObject = WeiXinUtils.doGetStr(url);
            //翻译的源语言
            String fromLanguage = jsonObject.getString("from");
            //翻译的目标语言
            String toLanguage = jsonObject.getString("to");
            //翻译的结果
            JSONArray transResult = jsonObject.getJSONArray("trans_result");
            String transSrc = "";
            String transDis = "";
            //拼接翻译结果
            StringBuilder stringBuilder = new StringBuilder();

            for(int i =0 ; i<transResult.size() ; i++){
                JSONObject o = (JSONObject) transResult.get(i);
                transSrc = o.getString("src");
                transDis = o.getString("dst");
                stringBuilder.append("翻译的内容为("+ fromLanguage +"):"+ transSrc +"\n");
                stringBuilder.append("翻译的结果为("+ toLanguage +"):"+ transDis +"\n");
            }
            return stringBuilder.toString();

        }
}

package com.hnu.scw.baidutranslate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TransApi {
    //百度翻译的接口URL
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    /**
     * 获取翻译的结果
     * @param query
     * @param from
     * @param to
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getTransResult(String query, String from, String to) throws UnsupportedEncodingException {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    /**
     * 设置URL请求的参数
     * @param query
     * @param from
     * @param to
     * @return
     * @throws UnsupportedEncodingException
     */
    private Map<String, String> buildParams(String query, String from, String to) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", appid);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 加密前的原文
        String src = appid + query + salt + securityKey;
        //MD5签名
        params.put("sign", MD5.md5(src));
        return params;
    }

}

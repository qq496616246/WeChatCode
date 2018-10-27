package com.hnu.scw.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author scw
 * @create 2018-01-17 9:28
 * @desc 检查微信和服务器是否链接成功
 **/
public class CheckConnectUtils {
    private static final String token = "wechat";

    /**
     * 判断是否链接匹配
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkConncetWithWeChat(String signature,String timestamp,String nonce){
        String[] arr = new String[]{token,timestamp,nonce};
        //排序
        Arrays.sort(arr);
        //生成字符串
        StringBuilder stringBuilder = new StringBuilder();
        for (String str:arr) {
            stringBuilder.append(str);
        }
        //进行SHA1加密
        String encodeString = passSha1Encode(stringBuilder.toString());
        if(signature.equals(encodeString)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 字符串进行SHA1加密
     * @param str
     * @return
     */
    public static String passSha1Encode(String str){
        if(str == null || str.length() == 0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9'
        ,'a','b','c','d','e','f'};
        try{
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes());
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j*2];
            int k = 0;
            for(int i=0 ; i <j ; i++){
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>>4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
           return null;
        }
    }
}

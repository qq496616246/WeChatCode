package com.hnu.scw.service;
import com.hnu.scw.bean.WeiXinUser;
import java.util.Map;

/**
 * 用于进行微信用户个人信息的操作接口
 */
public interface WeiXinUserInfoService {
     /**
      * 获取到微信个人用户的信息
      * @param accessToken
      * @param openId
      * @return
      */
     WeiXinUser getUserInfo(String accessToken, String openId);

     /**
      *用于获取网页授权后的信息字段，其中主要是获取openId
      * @param code  授权码
      * @return
      */
     Map<String , String > getAuthInfo(String code);

     /**
      * 进行网页授权的认证
      * @param code 授权码
      * @return
      */
     Map<String,String> oauth2GetOpenid(String code);
}

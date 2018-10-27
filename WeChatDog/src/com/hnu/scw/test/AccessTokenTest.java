package com.hnu.scw.test;

import com.hnu.scw.model.AccessToken;
import com.hnu.scw.utils.WeiXinAccessTokenKeepAlive;
import com.hnu.scw.utils.WeiXinUtils;
import org.junit.Test;

/**
 * @author scw
 * @create 2018-01-18 15:21
 * @desc 用于对Access_token内容相关的测试
 **/
public class AccessTokenTest {
    /**
     * 获取到Access_Token，这个对于要想使用其他的微信接口，就必须要有这个进行验证
     */
    @Test
    public void getAccssTokenTest(){
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        System.out.println("token:" +accessToken.getToken());
        System.out.println("有效时间:" +accessToken.getExpireIn());
    }



}

package com.hnu.scw.test;

import com.hnu.scw.model.AccessToken;
import com.hnu.scw.utils.PictureUtils;
import com.hnu.scw.utils.WeiXinUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author scw
 * @create 2018-01-18 15:23
 * @desc 对于图片，视频，音频，音乐上传到微信的接口测试，
 * 这个主要是用于对消息回复的内容的多媒体回复功能
 **/
public class MediaTest {
    @Test
    public void getAccssTokenTest(){
        //获取得到Access_Token
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        //获取到上传图片的media_id，这个是方便进行多媒体文件需要的一个ID（XML中需要）
        try {
            String imageId = PictureUtils.upLoadImage("E:/1.jpg", accessToken.getToken(), "thumb");
            System.out.println(imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

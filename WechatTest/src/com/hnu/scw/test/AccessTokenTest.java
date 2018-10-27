package com.hnu.scw.test;

import com.hnu.scw.constfild.ConstFildUtils;
import com.hnu.scw.model.AccessToken;
import com.hnu.scw.utils.PictureUtils;
import com.hnu.scw.utils.WeiXinUtils;
import net.sf.json.JSONObject;
import org.junit.Test;
import java.io.IOException;

/**
 * @author scw
 * @create 2018-01-17 14:39
 * @desc 用于测试access_token是否获取
 **/
public class AccessTokenTest {
    @Test
    public void getAccssTokenTest(){
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        System.out.println("token:" +accessToken.getToken());
        System.out.println("有效时间:" +accessToken.getExpireIn());
        //获取到上传图片的media_id
        try {
            String imageId = PictureUtils.upLoadImage("E:/1.jpg", accessToken.getToken(), "thumb");
            System.out.println(imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建菜单
     */
    @Test
    public void creatMenuTest(){
        //获取到access_token
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        //获取到自定义菜单的格式（JSONObject将对象转为json，然后再需要转为字符串型）
        String menu = JSONObject.fromObject(WeiXinUtils.initMenu()).toString();
        //调用创建菜单
        int result = WeiXinUtils.createMenu(accessToken.getToken(), menu);
        if(result == 0){
            //如果调用方法之后，返回的是0，那么就表示创建成功。
            System.out.println("创建成功");
        }else{
            System.out.println("创建失败");
        }
    }

    /**
     * 查询菜单
     */
    @Test
    public void queryMenuTest(){
        //获取到access_token
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        //调用菜单查询的方法，返回是的一个Json格式
        JSONObject jsonObject = WeiXinUtils.queryMenu(accessToken.getToken());
        System.out.println(jsonObject);
    }

    /**
     * 删除菜单
     */
    @Test
    public void deleteMenuTest(){
        //获取到access_token
        AccessToken accessToken = WeiXinUtils.getAccessToken();
        //调用菜单查询的方法，返回是的一个Json格式
        JSONObject jsonObject = WeiXinUtils.deleteMenu(accessToken.getToken());
        if(jsonObject.getInt("errcode") == 0){
            //返回0，表示的是删除成功
            System.out.println("删除成功");
        }else{
            System.out.println("删除失败");
        }
    }
}

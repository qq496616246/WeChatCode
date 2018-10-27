package com.hnu.scw.utils;

import com.hnu.scw.menu.BaseButton;
import com.hnu.scw.menu.ClickButton;
import com.hnu.scw.menu.CustomeMenu;
import com.hnu.scw.menu.ViewButton;
import com.hnu.scw.model.AccessToken;
import com.hnu.scw.projectconst.ProjectConst;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author scw
 * @create 2018-01-17 14:13
 * @desc 用户获取access_token,众号调用各接口时都需使用access_token
 **/
public class WeiXinUtils {
    /**
     * 微信公众号的APPID和Appsecret，这个是每个微信公众号都唯一的，以后配置不同的公众号配置这里即可
     */
    private static final String APPID = "wxe4930bbcd2ea4f37";
    private static final String APPSECRET = "b401c279f4a07b01721c16eb172391d9";
    //获取access_token的URL
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //进行创建菜单的接口URL
    private static final String CREATE_MENU_URL ="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //菜单查询的接口URL
    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    //菜单删除的接口URL
    private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    /**
     * Get请求，方便到一个url接口来获取结果
     * @param url
     * @return
     */
    public static JSONObject doGetStr(String url){
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try{
            HttpResponse response = defaultHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                String result = EntityUtils.toString(entity, "UTF-8");
                jsonObject = JSONObject.fromObject(result);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 带参数的post请求，方便到一个url接口来获取结果
     * @param url
     * @param outStr
     * @return
     */
    public static JSONObject doPostStr(String url , String outStr)  {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        try {
            httpPost.setEntity(new StringEntity(outStr , "UTF-8"));
            HttpResponse response = defaultHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                String result = EntityUtils.toString(entity, "UTF-8");
                jsonObject = JSONObject.fromObject(result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取access_token
     * @return
     */
    public static AccessToken getAccessToken(){
        AccessToken accessToken = new AccessToken();
        String url = ACCESS_TOKEN_URL.replace("APPID" ,APPID).replace("APPSECRET",APPSECRET);
        JSONObject jsonObject = doGetStr(url);
        if(jsonObject !=null){
            accessToken.setToken(jsonObject.getString("access_token"));
            accessToken.setExpireIn(jsonObject.getLong("expires_in"));
        }
        return accessToken;
    }

    /**
     * 设置菜单的形式
     * @return
     */
    public static CustomeMenu initMenu(){
        CustomeMenu customeMenu = new CustomeMenu();

        ClickButton clickButton = new ClickButton();
        clickButton.setName("click菜单");
        clickButton.setType("click");
        clickButton.setKey("01");

        ViewButton viewButton = new ViewButton();
        viewButton.setName("view菜单");
        viewButton.setType("view");
        //viewButton.setUrl("http://myjava.ngrok.xiaomiqiu.cn/tomainpage");
        String backUrl = "http://chatdog.ngrok.xiaomiqiu.cn/getuserinfo";
        //把这里的地址，需要设置成授权页面+回调地址，并且要把回调地址进行编码
        viewButton.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ ProjectConst.PROJECT_APPID+"&redirect_uri="+ URLEncoder.encode(backUrl)+"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");


        ViewButton viewButton1 = new ViewButton();
        viewButton1.setName("view1菜单");
        viewButton1.setType("view");
        viewButton1.setUrl("http://chatdog.ngrok.xiaomiqiu.cn/queryproduct");

        ClickButton clickButton2 = new ClickButton();
        clickButton2.setName("扫码事件的click菜单");
        clickButton2.setType("scancode_push");
        clickButton2.setKey("02");

        ClickButton clickButton3 = new ClickButton();
        clickButton3.setName("地理位置的click菜单");
        clickButton3.setType("location_select");
        clickButton3.setKey("03");

        BaseButton baseButton = new BaseButton();
        baseButton.setName("菜单");
        //将clickButton2,clickButton3作为一个子菜单中的按钮
        baseButton.setSub_button(new BaseButton[]{viewButton1,clickButton2,clickButton3});
        //把按钮分别放入到菜单中
        customeMenu.setButton(new BaseButton[]{clickButton,viewButton,baseButton});

        return customeMenu;
    }

    /**
     * 创建自定义菜单
     * @param token
     * @param menu
     * @return
     */
    public static int createMenu(String token , String menu){
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN" ,token);
        JSONObject jsonObject = doPostStr(url, menu);
        if(jsonObject != null){
            //接受返回回来的参数，如果是0，就是创建成功
            result = jsonObject.getInt("errcode");
        }
        return result;
    }

    /**
     * 对菜单进行查询
     * @param token
     * @return
     */
    public static JSONObject queryMenu(String token){
        String url = QUERY_MENU_URL.replace("ACCESS_TOKEN" ,token);
        JSONObject jsonObject = doGetStr(url);
        return jsonObject;
    }

    /**
     * 对菜单进行删除
     * @param token
     * @return
     */
    public static JSONObject deleteMenu(String token){
        String url = DELETE_MENU_URL.replace("ACCESS_TOKEN" ,token);
        JSONObject jsonObject = doGetStr(url);
        return jsonObject;
    }
}

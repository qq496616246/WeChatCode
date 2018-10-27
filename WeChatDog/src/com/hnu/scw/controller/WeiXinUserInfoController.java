package com.hnu.scw.controller;

import com.hnu.scw.bean.WeiXinUser;
import com.hnu.scw.service.WeiXinUserInfoService;
import com.hnu.scw.utils.WeiXinUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author scw
 * @create 2018-01-18 17:47
 * @desc 获取微信用户的所有信息，这个主要是为了不要用户自己填写个人信息
 **/
@Controller
public class WeiXinUserInfoController {

    @Autowired
    private WeiXinUserInfoService userService;

    /**
     * 进行网页授权，便于获取到用户的绑定的内容
     * @param request
     * @param session
     * @param map
     * @return
     */
    @RequestMapping("/getuserinfo")
    public String check(HttpServletRequest request , HttpSession session, Map<String, Object> map) {
        //首先判断一下session中，是否有保存着的当前用户的信息，有的话，就不需要进行重复请求信息
        WeiXinUser  weiXinUser = null ;
        if(session.getAttribute("currentUser") != null){
            weiXinUser = (WeiXinUser) session.getAttribute("currentUser");
        }else {
            /**
             * 进行获取openId，必须的一个参数，这个是当进行了授权页面的时候，再重定向了我们自己的一个页面的时候，
             * 会在request页面中，新增这个字段信息，要结合这个ProjectConst.Get_WEIXINPAGE_Code这个常量思考
             */
            String code = request.getParameter("code");
            try {
                //得到当前用户的信息(具体信息就看weixinUser这个javabean)
                weiXinUser = getTheCode(code);
                //将获取到的用户信息，放入到session中
                session.setAttribute("currentUser", weiXinUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put("weiXinUser", weiXinUser);
        return "hello";
    }

    /**
     * 获取用户的openId
     * @param code
     * @return 返回封装的微信用户的对象
     */
    private WeiXinUser getTheCode(String code) {
        Map<String , String>  authInfo = new HashMap<>();
        String openId = "";
        if (code != null)
        {
            // 调用根据用户的code得到需要的授权信息
            authInfo= userService.getAuthInfo(code);
           //获取到openId
            openId = authInfo.get("Openid");
        }

         /*获取基础刷新的接口访问凭证
         （为什么不用authInfo.get("AccessToken");
         因为这里面的access_token和基础的那个access_token不一样，
         这里获取的是授权的token）*/
        String accessToken = WeiXinUtils.getAccessToken().getToken();
        //String accessToken = authInfo.get("AccessToken");
        //获取到微信用户的信息
        WeiXinUser userinfo = userService.getUserInfo(accessToken ,openId);
        return userinfo;
    }


}

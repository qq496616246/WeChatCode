package com.hnu.scw.projectconst;

/**
 * @author scw
 * @create 2018-01-18 15:31
 * @desc 项目相关的静态量
 **/
public class ProjectConst {
    /**
     *项目的IP地址，方便以后进行更换
     */
    public static final String PROJECT_IP = "http://mychat.ngrok.xiaomiqiu.cn";
    /**
     *项目的Accss_token地址，方便调用其他接口的时候进行使用（这个是动态改变的，所以这个没什么实际作用）
     */
    public static String ACCESS_TOKEN_VALUE = "5_j8S2sthvdn5e10Y7VAKQcj_tR7v174IFXstmrdch9SnmJLsridAyVEI3WTLtlnkB6GNz09OL0WzC5BQFGMQwv3ijIom_VK7vhNs55Rr3Z5Vg8TwOYZ78v8zRghnJQkoGp1uQg5UirCrZcTM5FVAfAJASIN";

    /**
     * 微信公众号的APPID和appsecret
     */
    public static final String PROJECT_APPID = "wxe4930bbcd2ea4f37";
    public static final String PROJECT_APPSECRET = "b401c279f4a07b01721c16eb172391d9";

    /**
     * 用于获取当前与微信公众号交互的用户信息的接口（一般是用第一个接口地址）
     */
    public static final String GET_WEIXIN_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
    public final static String Get_Page_Users_Url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 用于进行网页授权验证的接口URL，通过这个才可以得到openID等字段信息
     */
    public final static String GET_WEBAUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    /**
     * 用于进行当点击按钮的时候，能够在网页授权之后获取到code，再跳转到自己设定的一个URL路径上的接口，这个主要是为了获取之后于
     * 获取openId的接口相结合
     * 注意：参数：toselfURL  表示的是当授权成功后，跳转到的自己设定的页面，所以这个要根据自己的需要进行修改
     */
    public final static String Get_WEIXINPAGE_Code = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=toselfURL&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
    /**
     * 获取access_token的URL
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

}

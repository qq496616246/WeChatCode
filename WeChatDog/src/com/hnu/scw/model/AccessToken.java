package com.hnu.scw.model;

/**
 * @author scw
 * @create 2018-01-17 14:35
 * @desc 封装AccessToken的实体
 **/
public class AccessToken {
    /**
     * accss_token的值
     */
    private String token;

    /**
     * access_token保存的有效时间
     * 如果不设置的话，微信默认是2个小时有效，所以如果超过获取时间+2小时，那么必须重新获取
     */
    private Long expireIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Long expireIn) {
        this.expireIn = expireIn;
    }
}

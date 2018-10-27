package com.hnu.scw.model;

/**
 * @author scw
 * @create 2018-01-17 14:35
 * @desc 封装AccessToken的实体
 **/
public class AccessToken {
    private String token;
    private int expireIn;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }
}

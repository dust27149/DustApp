package com.dust.app.retrofit.response;

import com.dust.app.bean.UserInfo;

public class LoginResponse extends Response {

    public UserInfo data;
    public String refreshToken;
    public String token;

}

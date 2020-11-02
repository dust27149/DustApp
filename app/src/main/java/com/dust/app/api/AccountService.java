package com.dust.app.api;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.base.Response;
import com.dust.app.bean.CommonResponse;
import com.dust.app.bean.LoginResponse;
import com.dust.app.bean.UserInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountService {

    @POST("test/getVerification")
    Observable<Response> getVerification(@Body JSONObject params);

    @POST("test/register")
    Observable<Response> register(@Body JSONObject params);

    @POST("test/unregister")
    Observable<Response> unregister(@Body JSONObject params, @Query("token") String token);

    @POST("test/login")
    Observable<LoginResponse> login(@Body JSONObject params);

    @GET("test/logout")
    Observable<Response> logout(@Query("token") String token);

    @POST("test/bindPhoneNumber")
    Observable<Response> bindPhoneNumber(@Body JSONObject params, @Query("token") String token);

    @POST("test/loginByPhoneNumber")
    Observable<LoginResponse> loginByPhoneNumber(@Body JSONObject params);

    @POST("test/resetPhoneNumber")
    Observable<Response> resetPhoneNumber(@Body JSONObject params, @Query("token") String token);

    @POST("test/bindWeChat")
    Observable<LoginResponse> bindWeChat(@Body JSONObject params);

    @POST("test/loginByWeChat")
    Observable<LoginResponse> loginByWeChat(@Body JSONObject params);

    @POST("test/resetWeChat")
    Observable<Response> resetWeChat(@Body JSONObject params, @Query("token") String token);

    @GET("test/refreshToken")
    Observable<CommonResponse<String>> refreshToken(@Query("token") String token);

    @GET("test/getUserInfoList")
    Observable<CommonResponse<List<UserInfo>>> getUserInfoList(@Query("token") String token);

}

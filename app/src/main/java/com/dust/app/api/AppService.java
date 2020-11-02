package com.dust.app.api;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.bean.AppInfo;
import com.dust.app.bean.CommonResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AppService {

    @POST("test/getAppInfo")
    Observable<CommonResponse<AppInfo>> getAppInfo(@Body JSONObject params);

}

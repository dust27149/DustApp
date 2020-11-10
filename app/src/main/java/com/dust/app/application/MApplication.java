package com.dust.app.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.dust.app.utils.SharedPreferencesUtils;

public class MApplication extends Application {

    private final String TAG = MApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesUtils.init(this);
        initCloudChannel(this);
    }

    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, "init cloudChannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.e(TAG, "init cloudChannel failed!errorCode:" + errorCode + ",errorMessage:" + errorMessage);
            }
        });

    }

}

package com.dust.app.retrofit;

import com.dust.app.api.AccountService;
import com.dust.app.api.AppService;
import com.dust.app.bean.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }

    public static AccountService getAccountService() {
        return createApi(AccountService.class, Constant.getMainServer());
    }

    public static AppService getAppService() {
        return createApi(AppService.class, Constant.getUpdateServer());
    }

    private static <T> T createApi(Class<T> clazz, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return retrofit.create(clazz);
    }

    private static void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

}

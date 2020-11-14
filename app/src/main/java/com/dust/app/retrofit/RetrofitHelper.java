package com.dust.app.retrofit;

import com.dust.app.BuildConfig;
import com.dust.app.retrofit.api.AccountService;
import com.dust.app.retrofit.api.AppService;
import com.dust.app.retrofit.api.TianDiTuService;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {

    public static final String IP_PATTERN = "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)";
    public static final String IP_PORT_PATTERN = "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d):([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])";
    public static final String HOSTNAME_PATTERN = "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
    public static String HOSTNAME = BuildConfig.DEFAULT_HOSTNAME;

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }

    public static AccountService getAccountService() {
        return createApi(AccountService.class, getMainServer());
    }

    public static AppService getAppService() {
        return createApi(AppService.class, getUpdateServer());
    }

    public static TianDiTuService getTianDiTuService() {
        return createApi(TianDiTuService.class, getTianDiTuServer());
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

    public static String getMainServer() {
        if (Pattern.matches(IP_PATTERN, HOSTNAME)) {
            return "http://" + HOSTNAME + ":8080";
        } else if (Pattern.matches(IP_PORT_PATTERN, HOSTNAME)) {
            return "http://" + HOSTNAME;
        } else if (Pattern.matches(HOSTNAME_PATTERN, HOSTNAME)) {
            return "https://" + HOSTNAME;
        }
        return "http://" + BuildConfig.DEFAULT_HOSTNAME + ":8080";
    }

    public static String getUpdateServer() {
        return getMainServer();
    }

    public static String getMqttServer() {
        return "tcp://" + BuildConfig.DEFAULT_HOSTNAME + ":1883";
    }

    public static String getTianDiTuServer() {
        return "http://" + BuildConfig.tianDiTuServer;
    }

}

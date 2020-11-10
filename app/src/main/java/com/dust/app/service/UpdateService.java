package com.dust.app.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.R;
import com.dust.app.bean.AppInfo;
import com.dust.app.bean.Constant;
import com.dust.app.retrofit.RetrofitHelper;
import com.dust.app.utils.NotificationUtils;
import com.dust.app.utils.PackageUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UpdateService extends Service {

    private final String TAG = UpdateService.class.getSimpleName();
    private CompositeDisposable compositeDisposable;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        checkUpdate(PackageUtils.getVersionName(this));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        compositeDisposable.clear();
    }

    private void checkUpdate(String appVersion) {
        Log.i(TAG, "checkUpdate");
        JSONObject params = new JSONObject();
        params.put("appId", Constant.APP_ID);
        Disposable disposable = RetrofitHelper.getAppService().getAppInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.success) {
                        Log.i(TAG, response.message);
                        Constant.updateInfo = response.data;
                        int code = PackageUtils.compareVersion(appVersion, response.data.appVersion);
                        if (code == 1) {
                            showUpdateNotification(response.data);
                            stopSelf();
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, throwable.toString());
                    throwable.printStackTrace();
                });
        compositeDisposable.add(disposable);
    }

    private void showUpdateNotification(AppInfo appInfo) {

        Intent intent = new Intent(this, InstallService.class);
        intent.putExtra("download_url", appInfo.downloadUrl);
        intent.putExtra("app_name", appInfo.appName);
        intent.putExtra("notification_id", NotificationUtils.getUpdateNotificationId());
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationUtils.getUpdateChannel(this))
                .setContentTitle("发现新版本：" + Constant.updateInfo.appVersion)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Constant.updateInfo.updateInfo))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logo)
                .setOnlyAlertOnce(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo))
                .addAction(R.drawable.ic_logo, getString(R.string.update), pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NotificationUtils.getUpdateNotificationId(), builder.build());

    }

}

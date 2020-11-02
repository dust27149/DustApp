package com.dust.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.dust.app.utils.FileUtils;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InstallService extends Service {

    private final String TAG = InstallService.class.getSimpleName();
    private Disposable downDisposable;
    private File file;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            file = FileUtils.createFile(getExternalFilesDir("update"), extras.getString("app_name") + ".apk");
            downloadApk(extras.getString("download_url"));
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(extras.getInt("notification_id"));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "service destroy");
    }

    private void downloadApk(String downloadUrl) {
        Observable
                .create((ObservableOnSubscribe<Integer>) emitter -> FileUtils.download(downloadUrl, file, emitter))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        downDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Integer result) {
                        Log.e(TAG, "进度" + result + "%");
                        if (result == 100) {
                            FileUtils.installApk(getApplication(), file);
                            downDisposable.dispose();
                            stopSelf();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError" + e.getMessage());
                        Toast.makeText(getApplication(), "网络异常！请重新下载！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }

}

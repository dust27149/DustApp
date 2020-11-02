package com.dust.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import io.reactivex.rxjava3.core.ObservableEmitter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();
    private static long downloadLength = 0;
    private static long contentLength = 0;

    public static File createFile(File filePath, String fileName) {
        File file = new File(filePath, fileName);
        if (file.exists()) {
            boolean delete = file.delete();
            Log.i(TAG, "delete:" + delete);
        } else {
            try {
                boolean createNewFile = file.createNewFile();
                Log.i(TAG, "createNewFile:" + createNewFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void installApk(Context context, File file) {
        try {
            Uri uri = FileProvider.getUriForFile(context, "com.dust.app.fileProvider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void download(String downloadUrl, File file, ObservableEmitter<Integer> emitter) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "下载失败" + e.getMessage());
                breakpoint(downloadUrl, file, emitter);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.body() == null) {
                    Log.e(TAG, "下载失败,response" + JSON.toJSONString(response));
                    breakpoint(downloadUrl, file, emitter);
                    return;
                }
                InputStream is = null;
                FileOutputStream fos = null;
                byte[] buff = new byte[2048];
                int len;
                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    long total = response.body().contentLength();
                    contentLength = total;
                    long sum = 0;
                    while ((len = is.read(buff)) != -1) {
                        fos.write(buff, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        emitter.onNext(progress);
                        downloadLength = sum;
                    }
                    fos.flush();
                } catch (Exception e) {
                    Log.e(TAG, "下载失败,e:" + e.getMessage());
                    e.printStackTrace();
                    breakpoint(downloadUrl, file, emitter);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private static void breakpoint(String downloadUrl, File file, ObservableEmitter<Integer> emitter) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                breakpoint(downloadUrl, file, emitter);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.body() == null) {
                    breakpoint(downloadUrl, file, emitter);
                    return;
                }
                InputStream is = null;
                RandomAccessFile randomFile = null;
                byte[] buff = new byte[2048];
                int len;
                try {
                    is = response.body().byteStream();
                    randomFile = new RandomAccessFile(file, "rwd");
                    randomFile.seek(downloadLength);
                    long total = contentLength;
                    long sum = downloadLength;
                    while ((len = is.read(buff)) != -1) {
                        randomFile.write(buff, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        emitter.onNext(progress);
                        downloadLength = sum;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    breakpoint(downloadUrl, file, emitter);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (randomFile != null)
                            randomFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}

package com.dust.app.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.dust.app.R;
import com.dust.app.activity.SettingActivity;
import com.dust.app.utils.NotificationUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class PushReceiver extends MessageReceiver {

    public static final String TAG = PushReceiver.class.getSimpleName();

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        Log.e(TAG, "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
        showNotification(context, title, summary, extraMap);
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.e(TAG, "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e(TAG, "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e(TAG, "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e(TAG, "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e(TAG, "onNotificationRemoved");
    }

    private void showNotification(Context context, String title, String summary, Map<String, String> extraMap) {

        Intent intent = new Intent(context, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        new Thread(() -> {
            Bitmap bitmap = null;
            try {
                String url = extraMap.get("picture_url");
                URL iconUrl = new URL(url);
                URLConnection conn = iconUrl.openConnection();
                HttpURLConnection http = (HttpURLConnection) conn;
                int length = http.getContentLength();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, length);
                bitmap = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationUtils.getAlarmChannel(context))
                    .setContentTitle(title)
                    .setContentText(summary)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo))
                    .setContentIntent(pendingIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);
            if (bitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null).setSummaryText(summary));
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(NotificationUtils.getNotificationId(), builder.build());
        }).start();
    }

}

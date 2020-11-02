package com.dust.app.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;

import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationUtils {

    private static final String UPDATE_CHANNEL = "update_channel";
    private static final String ALARM_CHANNEL = "alarm_channel";

    private static final int UPDATE_NOTIFICATION_ID = 1;
    private final static AtomicInteger NOTIFICATION_ID = new AtomicInteger(0);

    public static int getUpdateNotificationId() {
        return UPDATE_NOTIFICATION_ID;
    }

    public static int getNotificationId() {
        return NOTIFICATION_ID.incrementAndGet();
    }

    public static String getUpdateChannel(Context context) {
        CharSequence CHANNEL_NAME = "App更新";
        String CHANNEL_DESC = "App更新通知";
        NotificationChannel channel = new NotificationChannel(UPDATE_CHANNEL, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNEL_DESC);
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400});
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.createNotificationChannel(channel);
        return UPDATE_CHANNEL;
    }

    public static String getAlarmChannel(Context context) {

        CharSequence CHANNEL_NAME = "CHANNEL_NAME";
        String CHANNEL_DESC = "CHANNEL_DESC";
        NotificationChannel channel = new NotificationChannel(ALARM_CHANNEL, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(CHANNEL_DESC);
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400});
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.createNotificationChannel(channel);
        return ALARM_CHANNEL;
    }


}

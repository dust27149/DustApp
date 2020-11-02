package com.dust.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String getNowDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getNowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

}

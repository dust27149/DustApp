package com.dust.app.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    private static Application context;
    private static String name;

    public static void init(Application application) {
        context = application;
        name = context.getPackageName();
    }

    public static void putString(String key, String value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }


    public static void putInt(String key, int value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt(key, value).apply();
    }


    public static int getInt(String key, int defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }


    public static void putFloat(String key, float value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putFloat(key, value).apply();
    }


    public static float getFloat(String key, float defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putLong(key, value).apply();
    }

    public static float getLong(String key, long defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void remove(String key) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.remove(key).apply();
    }

    public static void apply(String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.apply();
    }
}
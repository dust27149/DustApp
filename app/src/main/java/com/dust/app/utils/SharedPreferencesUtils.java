package com.dust.app.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    private static Application context;

    public static void init(Application application) {
        context = application;
    }

    public static void putString(String name, String key, String value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putString(key, value).apply();
    }

    public static String getString(String name, String key, String defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }


    public static void putInt(String name, String key, int value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt(key, value).apply();
    }


    public static int getInt(String name, String key, int defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }


    public static void putFloat(String name, String key, float value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putFloat(key, value).apply();
    }


    public static float getFloat(String name, String key, float defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    public static void putLong(String name, String key, long value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putLong(key, value).apply();
    }

    public static float getLong(String name, String key, long defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    public static void putBoolean(String name, String key, boolean value) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String name, String key, boolean defaultValue) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    public static void remove(String name, String key) {
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
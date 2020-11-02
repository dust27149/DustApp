package com.dust.app.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageUtils {

    private static Application context;

    public static void init(Application application) {
        context = application;
    }

    /**
     * 获取App名称
     *
     * @return App名称
     */
    public static String getAppName() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            int labelRes = applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本名称
     *
     * @return 版本名称
     */
    public static String getVersionName() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @return 版本号
     */
    public static int getVersionCode() {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较版本号
     *
     * @param localVersion  本地版本
     * @param serverVersion 云端版本
     * @return -1：错误 0：无更新 1：有更新
     */
    public static int compareVersion(String localVersion, String serverVersion) {
        if (localVersion == null || serverVersion == null) {
            return -1;
        }
        if (localVersion.equals(serverVersion)) {
            return 0;
        }
        String[] local = localVersion.split("\\.");
        String[] server = serverVersion.split("\\.");
        int minLen = Math.min(local.length, server.length);
        int index = 0, diff = 0;
        while (index < minLen && (diff = Integer.parseInt(local[index]) - Integer.parseInt(server[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < local.length; i++) {
                if (Integer.parseInt(local[i]) > 0) {
                    return -1;
                }
            }
            for (int i = index; i < server.length; i++) {
                if (Integer.parseInt(server[i]) > 0) {
                    return 1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? -1 : 1;
        }
    }

}

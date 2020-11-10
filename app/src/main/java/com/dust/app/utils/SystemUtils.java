package com.dust.app.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class SystemUtils {

    public static void setStatusBarFullTransparent(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    private static void showActivity(@NonNull Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    private static void showActivity(@NonNull Context context, String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return isIgnoring;
    }

    public static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void intentToSetting(Context context) {
        switch (Build.BRAND.toLowerCase()) {
            case "huawei":
            case "honor":
                try {
                    showActivity(context, "com.huawei.systemmanager",
                            "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
                } catch (Exception e) {
                    showActivity(context, "com.huawei.systemmanager",
                            "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
                }
                break;
            case "xiaomi":
                showActivity(context, "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity");
                break;
            case "oppo":
                try {
                    showActivity(context, "com.coloros.phonemanager");
                } catch (Exception e1) {
                    try {
                        showActivity(context, "com.oppo.safe");
                    } catch (Exception e2) {
                        try {
                            showActivity(context, "com.coloros.oppoguardelf");
                        } catch (Exception e3) {
                            showActivity(context, "com.coloros.safecenter");
                        }
                    }
                }
                break;
            case "vivo":
                showActivity(context, "com.iqoo.secure");
                break;
            case "meizu":
                showActivity(context, "com.meizu.safe");
                break;
            case "samsung":
                try {
                    showActivity(context, "com.samsung.android.sm_cn");
                } catch (Exception e) {
                    showActivity(context, "com.samsung.android.sm");
                }
                break;
            case "letv":
                showActivity(context, "com.letv.android.letvsafe",
                        "com.letv.android.letvsafe.AutobootManageActivity");
                break;
            case "smartisan":
                showActivity(context, "com.smartisanos.security");
                break;
        }

    }

}

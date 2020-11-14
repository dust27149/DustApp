package com.dust.app.bean;

import android.location.Location;

public class Constant {

    public static AppInfo updateInfo;
    public static UserInfo userInfo;
    public static Location location;
    public static String TOKEN;
    public static final int WX_REQ = 2001;
    public static final int REQUEST_PERMISSION_CODE = 12345;

    public static boolean isOnline = false;
    public static boolean isInTask = false;
    public static boolean isBusy = false;

    public static int getUserStatus() {
        double userStatus = (isOnline ? 1 : 0) * Math.pow(2, 0) + (isInTask ? 1 : 0) * Math.pow(2, 1) + (isBusy ? 1 : 0) * Math.pow(2, 2);
        return (int) userStatus;
    }

}

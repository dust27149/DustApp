package com.dust.app.bean;

import android.location.Location;

import java.util.regex.Pattern;

public class Constant {

    public static final String DEFAULT_HOSTNAME = "192.168.1.187";
    public static final String IP_PATTERN = "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)";
    public static final String IP_PORT_PATTERN = "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d):([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])";
    public static final String HOSTNAME_PATTERN = "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
    public static String HOSTNAME = DEFAULT_HOSTNAME;

    public static final String APP_ID = "wx4cb5c5eeb3455cde";
    public static AppInfo updateInfo;
    public static UserInfo userInfo;
    public static Location location;
    public static String TOKEN;
    public static final int WX_REQ = 2001;
    public static final int REQUEST_PERMISSION_CODE = 12345;

    public static boolean isOnline = false;
    public static boolean isInTask = false;
    public static boolean isBusy = false;

    public static String getMainServer() {
        if (Pattern.matches(IP_PATTERN, HOSTNAME)) {
            return "http://" + HOSTNAME + ":8080";
        } else if (Pattern.matches(IP_PORT_PATTERN, HOSTNAME)) {
            return "http://" + HOSTNAME;
        } else if (Pattern.matches(HOSTNAME_PATTERN, HOSTNAME)) {
            return "https://" + HOSTNAME;
        }
        return "http://" + DEFAULT_HOSTNAME + ":8080";
    }

    public static String getUpdateServer() {
        return Constant.getMainServer();
    }

    public static String getMqttServer() {
        return "tcp://192.168.1.38:1883";
    }

    public static int getUserStatus() {
        double userStatus = (isOnline ? 1 : 0) * Math.pow(2, 0) + (isInTask ? 1 : 0) * Math.pow(2, 1) + (isBusy ? 1 : 0) * Math.pow(2, 2);
        return (int) userStatus;
    }

}

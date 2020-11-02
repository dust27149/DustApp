package com.dust.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dust.app.bean.Constant;
import com.dust.app.database.DBOpenHelper;
import com.dust.app.utils.TimeUtils;

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;

public class GPSService extends Service {

    private static final String TAG = GPSService.class.getSimpleName();
    private GpsMyLocationProvider provider;
    private DBOpenHelper dbOpenHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        dbOpenHelper = new DBOpenHelper(this);
        initLocate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocate() {
        provider = new GpsMyLocationProvider(this);
//        provider.setLocationUpdateMinTime(10 * 1000);
//        provider.setLocationUpdateMinDistance(10);
        provider.startLocationProvider((location, source) -> {
            Log.i(TAG, "Location changed:" + location.getLongitude() + "," + location.getLatitude() + "," + location.getAltitude());
            Constant.location = location;
            String userID = String.valueOf(Constant.userInfo.id);
            String longitude = String.valueOf(location.getLongitude());
            String latitude = String.valueOf(location.getLatitude());
            String altitude = String.valueOf(location.getAltitude());
            String dateTime = TimeUtils.getNowDateTime();
            String userStatus = String.valueOf(Constant.getUserStatus());
            dbOpenHelper.getWritableDatabase()
                    .execSQL("INSERT INTO user_status(user_id,user_lon,user_lat,user_alt,time,user_status) VALUES(?,?,?,?,?,?)",
                            new String[]{userID, longitude, latitude, altitude, dateTime, userStatus});
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        provider.stopLocationProvider();
        stopSelf();
    }

}

package com.dust.app.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.bean.Constant;
import com.dust.app.bean.UserStatus;
import com.dust.app.database.DBOpenHelper;
import com.dust.app.retrofit.RetrofitHelper;
import com.dust.app.utils.TimeUtils;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MQTTService extends Service {

    private static final String TAG = MQTTService.class.getSimpleName();
    private MqttClient client;
    private String topic;
    private Timer timer;
    private DBOpenHelper dbOpenHelper;
    private UserStatus userStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        dbOpenHelper = new DBOpenHelper(this);
        String serverURI = RetrofitHelper.getMqttServer();
        String client_id = "android_client" + Constant.userInfo.id;
        String username = "username";
        String password = "password";
        topic = "topic";
        userStatus = new UserStatus(Constant.userInfo.id);
        init(serverURI, client_id, username, password);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        try {
            timer.cancel();
            pushUserStatus();
            client.disconnect();
        } catch (MqttException e) {
            Log.e(TAG, "disconnect failed:" + e.getMessage());
        }
        stopSelf();
    }

    private void init(String serverURI, String client_id, String username, String password) {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient(serverURI, client_id, persistence);
            client.setCallback(callback);
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.getBytes());
            connOpts.setAutomaticReconnect(true);
            connOpts.setConnectionTimeout(30);
            connOpts.setKeepAliveInterval(20);
            MqttMessage lastWill = new MqttMessage("connect lost".getBytes());
            connOpts.setWill(topic, lastWill);
            Log.i(TAG, "Connecting to: " + serverURI);
            client.connect(connOpts);
            Constant.isOnline = true;
        } catch (MqttException me) {
            Log.e(TAG, "initException: " + me.getMessage());
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic, 2);
        } catch (MqttException e) {
            Log.e(TAG, topic + "subscribeFailed！" + e.getMessage());
        }
    }

    public void unSubscribe(String topic) {
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            Log.e(TAG, topic + "unSubscribeFailed！" + e.getMessage());
        }
    }

    public void publish(String topic, String msg) throws Exception {
        try {
            client.publish(topic, msg.getBytes(), 2, false);
            Log.i(TAG, topic + ":" + msg);
        } catch (MqttException e) {
            Log.e(TAG, topic + "publishFailed" + e.getMessage());
            throw new Exception();
        }
    }

    private final MqttCallback callback = new MqttCallback() {
        @Override
        public void disconnected(MqttDisconnectResponse disconnectResponse) {
            Log.e(TAG, "disconnected: " + disconnectResponse.getException());
            timer.cancel();
            Constant.isOnline = false;
        }

        @Override
        public void mqttErrorOccurred(MqttException exception) {
            Log.e(TAG, "mqttErrorOccurred: " + exception.getLocalizedMessage());
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            Log.i(TAG, "messageArrived:" + new String(message.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttToken token) {
            Log.i(TAG, "deliveryComplete");
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Log.i(TAG, "connectComplete: " + (reconnect ? "reconnect to " : "connect to ") + serverURI);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pushUserStatus();
                }
            }, 0, 30 * 1000);
        }

        @Override
        public void authPacketArrived(int reasonCode, MqttProperties properties) {
            Log.i(TAG, "authPacketArrived ");
        }
    };

    private void pushUserStatus() {
        if (!client.isConnected()) {
            return;
        }
        try {
            dbOpenHelper.getWritableDatabase().beginTransaction();
            Cursor cursor = dbOpenHelper.getReadableDatabase().rawQuery("SELECT * FROM user_status WHERE user_id = ? AND has_been_published = ?",
                    new String[]{String.valueOf(userStatus.userId), "false"});
            List<UserStatus> userStatusList = new ArrayList<>();
            if (cursor.getCount() == 0) {
                userStatus.time = TimeUtils.getNowDateTime();
                userStatus.userStatus = Constant.getUserStatus();
                userStatusList.add(userStatus);
                cursor.close();
                publish(topic, JSONObject.toJSONString(userStatusList));
            } else {
                while (cursor.moveToNext()) {
                    userStatus.userLon = cursor.getDouble(cursor.getColumnIndex("user_lon"));
                    userStatus.userLat = cursor.getDouble(cursor.getColumnIndex("user_lat"));
                    userStatus.userAlt = cursor.getDouble(cursor.getColumnIndex("user_alt"));
                    userStatus.time = cursor.getString(cursor.getColumnIndex("time"));
                    userStatus.userStatus = cursor.getInt(cursor.getColumnIndex("user_status"));
                    userStatusList.add(userStatus);
                }
                cursor.close();
                publish(topic, JSONObject.toJSONString(userStatusList));
                dbOpenHelper.getWritableDatabase()
                        .execSQL("UPDATE user_status SET has_been_published =? WHERE user_id = ?",
                                new String[]{"true", String.valueOf(userStatus.userId)});
            }
            dbOpenHelper.getWritableDatabase().setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "execute SQL error:" + e.getLocalizedMessage());
        } finally {
            dbOpenHelper.getReadableDatabase().endTransaction();
        }
    }

}
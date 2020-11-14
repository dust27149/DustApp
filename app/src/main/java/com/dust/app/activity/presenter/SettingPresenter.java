package com.dust.app.activity.presenter;

import android.util.Log;

import com.dust.app.activity.contract.SettingContract;
import com.dust.app.retrofit.RetrofitHelper;
import com.dust.app.utils.SharedPreferencesUtils;

import java.util.regex.Pattern;

import static com.dust.app.retrofit.RetrofitHelper.HOSTNAME_PATTERN;
import static com.dust.app.retrofit.RetrofitHelper.IP_PATTERN;
import static com.dust.app.retrofit.RetrofitHelper.IP_PORT_PATTERN;

public class SettingPresenter implements SettingContract.Presenter {

    private final String TAG = SettingPresenter.class.getSimpleName();
    private final SettingContract.View view;

    public SettingPresenter(SettingContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void saveSettings(String server) {
        if (Pattern.matches(IP_PATTERN, server) || Pattern.matches(IP_PORT_PATTERN, server) || Pattern.matches(HOSTNAME_PATTERN, server)) {
            RetrofitHelper.HOSTNAME = server;
            Log.i(TAG, "HOSTNAME changed to" + server);
            SharedPreferencesUtils.putString("hostname", server);
            view.saveSettingsSuccess();
        } else {
            view.showToast("服务器地址错误！请重新检查！");
        }

    }

}

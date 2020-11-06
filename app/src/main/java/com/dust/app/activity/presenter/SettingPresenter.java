package com.dust.app.activity.presenter;

import android.util.Log;

import com.dust.app.activity.contract.SettingContract;
import com.dust.app.bean.Constant;
import com.dust.app.utils.SharedPreferencesUtils;

import java.util.regex.Pattern;

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
        if (Pattern.matches(Constant.IP_PATTERN, server) || Pattern.matches(Constant.IP_PORT_PATTERN, server) || Pattern.matches(Constant.HOSTNAME_PATTERN, server)) {
            Constant.HOSTNAME = server;
            Log.i(TAG, "HOSTNAME changed to" + server);
            SharedPreferencesUtils.putString("setting", "hostname", server);
            view.saveSettingsSuccess();
        } else {
            view.showToast("服务器地址错误！请重新检查！");
        }

    }

}

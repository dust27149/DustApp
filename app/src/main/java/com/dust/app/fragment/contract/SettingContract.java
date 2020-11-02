package com.dust.app.fragment.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;

public interface SettingContract {

    interface Presenter extends BasePresenter {

        void saveSettings(String server);

    }

    interface View extends BaseView<Presenter> {

        void saveSettingsSuccess();

    }
}

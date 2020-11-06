package com.dust.app.activity.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;
import com.dust.app.bean.AppInfo;

public interface VersionContract {

    interface Presenter extends BasePresenter {

        void checkUpdate(String appId, String appVersion);

    }

    interface View extends BaseView<Presenter> {

        void hasUpdate(AppInfo appInfo);

    }

}

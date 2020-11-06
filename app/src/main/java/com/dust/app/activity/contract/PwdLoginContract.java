package com.dust.app.activity.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;

public interface PwdLoginContract {

    interface Presenter extends BasePresenter {

        void login(String account, String password);

    }

    interface View extends BaseView<Presenter> {

        void loginSuccess();

    }

}

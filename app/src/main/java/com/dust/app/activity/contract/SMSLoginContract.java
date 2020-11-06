package com.dust.app.activity.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;

public interface SMSLoginContract {

    interface Presenter extends BasePresenter {

        void getVerification(String phoneNumber);

        void login(String phoneNumber, String verification);

    }

    interface View extends BaseView<Presenter> {

        void getVerificationSuccess();

        void loginSuccess();

    }

}

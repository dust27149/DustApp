package com.dust.app.fragment.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;

public interface RegisterContract {

    interface Presenter extends BasePresenter {

        void getVerification(String phoneNumber);

        void register(String account, String password, String phoneNumber, String verification);

    }

    interface View extends BaseView<Presenter> {

        void getVerificationSuccess();

        void registerSuccess();

    }

}

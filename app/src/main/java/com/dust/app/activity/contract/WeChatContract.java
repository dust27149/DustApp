package com.dust.app.activity.contract;


import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;

public interface WeChatContract {

    interface Presenter extends BasePresenter {

        void loginByWeChat(String code);

        void getVerification(String phoneNumber);

        void bindWeChat(String phoneNumber, String verification, String code);

    }

    interface View extends BaseView<Presenter> {

        void loginByWeChatSuccess();

        void getVerificationSuccess();

        void bindWeChatSuccess();

    }

}

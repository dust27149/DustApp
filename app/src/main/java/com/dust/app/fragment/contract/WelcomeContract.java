package com.dust.app.fragment.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;

public interface WelcomeContract {

    interface Presenter extends BasePresenter {

        void refreshToken(String token);

    }

    interface View extends BaseView<Presenter> {

        void refreshTokenSuccess();

    }

}

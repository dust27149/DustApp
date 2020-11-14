package com.dust.app.activity.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;

public interface MapContract {

    interface Presenter extends BasePresenter {

        void getAddress(double longitude, double latitude);

    }

    interface View extends BaseView<Presenter> {

        void getAddressSuccess(String address);

        void getAddressFailed();

    }

}

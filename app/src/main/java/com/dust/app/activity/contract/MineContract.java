package com.dust.app.activity.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;
import com.dust.app.bean.UserInfo;

import java.util.List;

public interface MineContract {

    interface Presenter extends BasePresenter {

        void getUserInfoList();

    }

    interface View extends BaseView<Presenter> {

        void getUserInfoListSuccess(List<UserInfo> userInfoList);

    }

}

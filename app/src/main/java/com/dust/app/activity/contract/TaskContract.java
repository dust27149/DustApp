package com.dust.app.activity.contract;

import com.dust.app.base.BasePresenter;
import com.dust.app.base.BaseView;

public interface TaskContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

    }
}

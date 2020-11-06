package com.dust.app.activity.presenter;

import android.util.Log;

import com.dust.app.activity.contract.MineContract;
import com.dust.app.bean.Constant;
import com.dust.app.retrofit.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MinePresenter implements MineContract.Presenter {

    private final String TAG = MinePresenter.class.getSimpleName();
    private final MineContract.View view;
    private CompositeDisposable compositeDisposable;

    public MinePresenter(MineContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void getUserInfoList() {
        Disposable disposable = RetrofitHelper.getAccountService().getUserInfoList(Constant.TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResponse -> {
                    if (listResponse.success) {
                        Log.i(TAG, listResponse.message);
                        view.getUserInfoListSuccess(listResponse.data);
                    } else {
                        Log.e(TAG, listResponse.message);
                    }
                    view.showToast(listResponse.message);
                }, throwable -> {
                    Log.e(TAG, "获取用户列表失败");
                    view.showToast("获取用户列表失败");
                });
        compositeDisposable.add(disposable);
    }
}

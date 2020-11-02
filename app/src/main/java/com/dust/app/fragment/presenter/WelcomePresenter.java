package com.dust.app.fragment.presenter;

import android.util.Log;

import com.dust.app.bean.Constant;
import com.dust.app.fragment.contract.WelcomeContract;
import com.dust.app.retrofit.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WelcomePresenter implements WelcomeContract.Presenter {

    private final String TAG = WelcomePresenter.class.getSimpleName();
    private final WelcomeContract.View view;
    private CompositeDisposable compositeDisposable;

    public WelcomePresenter(WelcomeContract.View view) {
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
    public void refreshToken(String token) {
        Disposable disposable = RetrofitHelper.getAccountService().refreshToken(token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response.success) {
                        Constant.TOKEN = response.data;
                        view.refreshTokenSuccess();
                    }
                }, throwable -> Log.e(TAG, throwable.toString()));
        compositeDisposable.add(disposable);
    }
}

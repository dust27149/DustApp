package com.dust.app.activity.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.activity.contract.RegisterContract;
import com.dust.app.retrofit.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterPresenter implements RegisterContract.Presenter {

    private final String TAG = RegisterPresenter.class.getSimpleName();
    private final RegisterContract.View view;
    private CompositeDisposable compositeDisposable;

    public RegisterPresenter(RegisterContract.View view) {
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
    public void getVerification(String phoneNumber) {
        JSONObject params = new JSONObject();
        params.put("phoneNumber", phoneNumber);
        Disposable disposable = RetrofitHelper.getAccountService().getVerification(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseResponse -> {
                    if (baseResponse.success) {
                        Log.i(TAG, baseResponse.message);
                        view.getVerificationSuccess();
                    } else {
                        Log.e(TAG, baseResponse.message);
                    }
                    view.showToast(baseResponse.message);
                }, throwable -> {
                    Log.e(TAG, "获取验证失败：" + throwable.getMessage());
                    view.showToast("获取验证失败");
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void register(String account, String password, String phoneNumber, String verification) {
        JSONObject params = new JSONObject();
        params.put("account", account);
        params.put("password", password);
        params.put("phoneNumber", phoneNumber);
        params.put("verification", verification);
        Disposable disposable = RetrofitHelper.getAccountService().register(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseResponse -> {
                    if (baseResponse.success) {
                        Log.i(TAG, baseResponse.message);
                        view.registerSuccess();
                    } else {
                        Log.e(TAG, baseResponse.message);
                    }
                    view.showToast(baseResponse.message);
                }, throwable -> {
                    Log.e(TAG, "注册失败：" + throwable.getMessage());
                    view.showToast("注册失败");
                });
        compositeDisposable.add(disposable);
    }
}

package com.dust.app.fragment.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.bean.Constant;
import com.dust.app.fragment.contract.SMSLoginContract;
import com.dust.app.retrofit.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SMSLoginPresenter implements SMSLoginContract.Presenter {

    private final String TAG = SMSLoginPresenter.class.getSimpleName();
    private final SMSLoginContract.View view;
    private CompositeDisposable compositeDisposable;

    public SMSLoginPresenter(SMSLoginContract.View view) {
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
                    Log.e(TAG, "获取验证码失败" + throwable.getLocalizedMessage());
                    view.showToast("获取验证码失败");
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void login(String phoneNumber, String verification) {
        JSONObject params = new JSONObject();
        params.put("phoneNumber", phoneNumber);
        params.put("verification", verification);
        Disposable disposable = RetrofitHelper.getAccountService().loginByPhoneNumber(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.success) {
                        Log.i(TAG, response.data.nickname + "已登录");
                        Constant.userInfo = response.data;
                        Constant.TOKEN = response.token;
                        view.loginSuccess();
                    } else {
                        Log.e(TAG, "登录失败：" + response.message);
                    }
                    view.showToast(response.message);
                }, throwable -> {
                    Log.e(TAG, "登录失败：" + throwable.toString());
                    view.showToast("登录失败");
                });
        compositeDisposable.add(disposable);
    }
}

package com.dust.app.fragment.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.bean.Constant;
import com.dust.app.fragment.contract.WeChatContract;
import com.dust.app.retrofit.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.dust.app.bean.Constant.APP_ID;

public class WeChatPresenter implements WeChatContract.Presenter {

    private final String TAG = WeChatPresenter.class.getSimpleName();
    private final WeChatContract.View view;
    private CompositeDisposable compositeDisposable;

    public WeChatPresenter(WeChatContract.View view) {
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
    public void loginByWeChat(String code) {
        JSONObject params = new JSONObject();
        params.put("appId", APP_ID);
        params.put("code", code);
        Disposable disposable = RetrofitHelper.getAccountService().loginByWeChat(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.success) {
                        Log.i(TAG, response.message);
                        Constant.userInfo = response.data;
                        view.loginByWeChatSuccess();
                    } else {
                        Log.e(TAG, response.message);
                    }
                    view.showToast(response.message);
                }, throwable -> {
                    Log.e(TAG, "微信登录失败" + throwable.toString());
                    view.showToast("微信登录失败");
                });
        compositeDisposable.add(disposable);
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
    public void bindWeChat(String phoneNumber, String verification, String code) {
        JSONObject params = new JSONObject();
        params.put("phoneNumber", phoneNumber);
        params.put("verification", verification);
        params.put("code", code);
        Disposable disposable = RetrofitHelper.getAccountService().bindWeChat(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.success) {
                        Log.i(TAG, response.message);
                        Constant.userInfo = response.data;
                        Constant.TOKEN = response.token;
                        view.bindWeChatSuccess();
                    } else {
                        Log.e(TAG, response.message);
                    }
                    view.showToast(response.message);
                }, throwable -> {
                    Log.e(TAG, "绑定微信失败");
                    view.showToast("绑定微信失败");
                });
        compositeDisposable.add(disposable);
    }

}
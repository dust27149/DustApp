package com.dust.app.fragment.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.bean.Constant;
import com.dust.app.fragment.contract.PwdLoginContract;
import com.dust.app.retrofit.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PwdLoginPresenter implements PwdLoginContract.Presenter {

    private final String TAG = PwdLoginPresenter.class.getSimpleName();
    private final PwdLoginContract.View view;
    private CompositeDisposable compositeDisposable;

    public PwdLoginPresenter(PwdLoginContract.View view) {
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
    public void login(String account, String password) {
        JSONObject params = new JSONObject();
        params.put("account", account);
        params.put("password", password);
        Disposable disposable = RetrofitHelper.getAccountService().login(params)
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

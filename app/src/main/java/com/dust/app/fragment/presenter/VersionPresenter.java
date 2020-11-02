package com.dust.app.fragment.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.bean.Constant;
import com.dust.app.fragment.contract.VersionContract;
import com.dust.app.retrofit.RetrofitHelper;
import com.dust.app.utils.PackageUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class VersionPresenter implements VersionContract.Presenter {

    private final String TAG = VersionPresenter.class.getSimpleName();
    private final VersionContract.View view;
    private CompositeDisposable compositeDisposable;

    public VersionPresenter(VersionContract.View view) {
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
    public void checkUpdate(String appId, String appVersion) {
        JSONObject params = new JSONObject();
        params.put("appId", appId);
        Disposable disposable = RetrofitHelper.getAppService().getAppInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.success) {
                        Log.i(TAG, response.message);
                        Constant.updateInfo = response.data;
                        int code = PackageUtils.compareVersion(appVersion, response.data.appVersion);
                        if (code == 1) {
                            view.hasUpdate(response.data);
                            view.showToast("发现新版本");
                        } else {
                            view.showToast("暂无更新");
                        }
                    } else {
                        Log.e(TAG, response.message);
                        view.showToast(response.message);
                    }
                }, throwable -> {
                    Log.e(TAG, "获取更新失败");
                    view.showToast("获取更新失败");
                });
        compositeDisposable.add(disposable);
    }

}

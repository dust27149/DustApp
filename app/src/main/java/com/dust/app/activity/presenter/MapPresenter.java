package com.dust.app.activity.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.dust.app.BuildConfig;
import com.dust.app.activity.contract.MapContract;
import com.dust.app.retrofit.RetrofitHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MapPresenter implements MapContract.Presenter {

    private final String TAG = MapPresenter.class.getSimpleName();
    private final MapContract.View view;
    private CompositeDisposable compositeDisposable;

    public MapPresenter(MapContract.View view) {
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
    public void getAddress(double longitude, double latitude) {
        Map<String, String> params = new HashMap<>();
        params.put("postStr", "{\"lon\":" + longitude + ",\"lat\":" + latitude + ",\"ver\":1}");
        params.put("type", "geocode");
        params.put("tk", BuildConfig.tianDiTuToken);
        Disposable disposable = RetrofitHelper.getTianDiTuService().getGeocode(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> {
                    if (response.status == 0) {
                        Log.i(TAG, JSONObject.toJSONString(response));
                        view.getAddressSuccess(response.result.formatted_address);
                    } else {
                        Log.e(TAG, JSONObject.toJSONString(response));
                        view.getAddressFailed();
                    }
                }, throwable -> {
                    Log.e(TAG, "获取地址失败" + throwable.getLocalizedMessage());
                    throwable.getLocalizedMessage();
                    view.getAddressFailed();
                });
        compositeDisposable.add(disposable);
    }
}

package com.dust.app.fragment.presenter;

import com.dust.app.fragment.contract.MapContract;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

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

}

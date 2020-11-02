package com.dust.app.fragment.presenter;

import com.dust.app.fragment.contract.InfoContract;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class InfoPresenter implements InfoContract.Presenter {

    private final String TAG = InfoPresenter.class.getSimpleName();
    private final InfoContract.View view;
    private CompositeDisposable compositeDisposable;

    public InfoPresenter(InfoContract.View view) {
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

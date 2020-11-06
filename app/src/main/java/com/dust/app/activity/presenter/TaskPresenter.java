package com.dust.app.activity.presenter;

import com.dust.app.activity.contract.TaskContract;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class TaskPresenter implements TaskContract.Presenter {

    private final String TAG = TaskPresenter.class.getSimpleName();
    private final TaskContract.View view;
    private CompositeDisposable compositeDisposable;

    public TaskPresenter(TaskContract.View view) {
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

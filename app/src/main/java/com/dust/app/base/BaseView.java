package com.dust.app.base;

public interface BaseView<T> {

    void setPresenter(T presenter);

    void showToast(String s);

}

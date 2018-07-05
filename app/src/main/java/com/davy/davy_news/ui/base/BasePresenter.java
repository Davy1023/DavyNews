package com.davy.davy_news.ui.base;

/**
 * author: Davy
 * date: 18/7/4
 */
public class BasePresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;
    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {

        if(mView != null){
            mView = null;
        }
    }
}

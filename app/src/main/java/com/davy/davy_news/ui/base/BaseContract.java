package com.davy.davy_news.ui.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface BaseContract {

    interface BasePresenter<T extends BaseContract.BaseView>{

        void attachView(T view);

        void detachView();
    }
    interface BaseView{

        void showLoading();

        void showSuccess();

        //失败重试
        void showFaild();

        //显示当前网络不可用
        void showNoNet();

        //重试
        void onRetry();

        /**
         * 绑定生命周期
         */

        <T> LifecycleTransformer<T> bindToLife();
    }

}

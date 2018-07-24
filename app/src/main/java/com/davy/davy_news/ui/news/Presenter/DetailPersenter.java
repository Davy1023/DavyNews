package com.davy.davy_news.ui.news.Presenter;

import com.davy.davy_news.net.NewsApi;
import com.davy.davy_news.ui.base.BasePresenter;
import com.davy.davy_news.ui.news.contract.DetailContract;

import javax.inject.Inject;

/**
 * author: Davy
 * date: 18/7/24
 */
public class DetailPersenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {

    NewsApi mNewsApi;

    @Inject
    public DetailPersenter(NewsApi newsApi){

        this.mNewsApi = newsApi;

    }
    @Override
    public void getData(String id, String action, int pullNum) {

    }
}

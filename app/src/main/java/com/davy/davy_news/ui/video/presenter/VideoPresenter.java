package com.davy.davy_news.ui.video.presenter;

import com.davy.davy_news.bean.VideoChannelBean;
import com.davy.davy_news.bean.VideoDetailBean;
import com.davy.davy_news.net.NewsApi;
import com.davy.davy_news.net.RxSchedulers;
import com.davy.davy_news.ui.base.BasePresenter;
import com.davy.davy_news.ui.video.contract.VideoContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author: Davy
 * date: 18/8/12
 */
public class VideoPresenter extends BasePresenter<VideoContract.View> implements VideoContract.Presenter {

    NewsApi mNewsApi;

    @Inject
    public VideoPresenter(NewsApi newsApi){

        this.mNewsApi = newsApi;
    }
    @Override
    public void getVideoChannel() {
        mNewsApi.getVideoChannel()
                .compose(RxSchedulers.<List<VideoChannelBean>>applySchedulers())
                .compose(mView.<List<VideoChannelBean>>bindToLife())
                .subscribe(new Observer<List<VideoChannelBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<VideoChannelBean> videoChannelBeans) {
                            mView.loadVideoChannel(videoChannelBeans);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getVideoDetails(final int page, String listType, String typeId) {
        mNewsApi.getVideoDetails(page,listType,typeId)
                .compose(RxSchedulers.<List<VideoDetailBean>>applySchedulers())
                .compose(mView.<List<VideoDetailBean>>bindToLife())
                .subscribe(new Observer<List<VideoDetailBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<VideoDetailBean> videoDetailBeans) {
                        if(page > 1){
                            mView.loadMoreVideoDetails(videoDetailBeans);
                        }else {
                            mView.loadVideoDetails(videoDetailBeans);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

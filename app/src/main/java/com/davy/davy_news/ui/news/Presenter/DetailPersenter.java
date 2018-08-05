package com.davy.davy_news.ui.news.Presenter;

import android.util.Log;

import com.davy.davy_news.bean.NewsDetail;
import com.davy.davy_news.net.BaseObserver;
import com.davy.davy_news.net.NewsApi;
import com.davy.davy_news.net.RxSchedulers;
import com.davy.davy_news.ui.base.BasePresenter;
import com.davy.davy_news.ui.news.contract.DetailContract;
import com.davy.davy_news.utils.NewsUtils;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * author: Davy
 * date: 18/7/24
 */
public class DetailPersenter extends BasePresenter<DetailContract.View> implements DetailContract.Presenter {

     static final String TAG = DetailPersenter.class.getSimpleName();

    NewsApi mNewsApi;

    @Inject
    public DetailPersenter(NewsApi newsApi){

        this.mNewsApi = newsApi;

    }
    @Override
    public void getData(String id, final String action, int pullNum) {

        mNewsApi.getNewsDetail(id, action, pullNum)
                .compose(RxSchedulers.<List<NewsDetail>>applySchedulers())
                .map(new Function<List<NewsDetail>, NewsDetail>() {
                    @Override
                    public NewsDetail apply(List<NewsDetail> newsDetails) throws Exception {
                        for (NewsDetail newsDetail : newsDetails) {
                            if (NewsUtils.isBannerNews(newsDetail)) {
                                mView.loadBannerData(newsDetail);
                            }
                            if (NewsUtils.isTopNews(newsDetail)) {
                                mView.loadTopNewsData(newsDetail);
                            }
                        }
                        return newsDetails.get(0);
                    }
                })
                .map(new Function<NewsDetail, List<NewsDetail.ItemBean>>() {
                    @Override
                    public List<NewsDetail.ItemBean> apply(NewsDetail newsDetail) throws Exception {
                        Iterator<NewsDetail.ItemBean> iterator = newsDetail.getItem().iterator();
                        while (iterator.hasNext()) {
                            try {
                                NewsDetail.ItemBean bean = iterator.next();
                                if (bean.getType().equals(NewsUtils.TYPE_DOC)) {
                                    if (bean.getStyle().getView() != null) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_TITLEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_TITLEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG;
                                        }
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_ADVERT)) {
                                    if (bean.getStyle() != null) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_TITLEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG;
                                        } else if (bean.getStyle().getView().equals(NewsUtils.VIEW_SLIDEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG;
                                        }
                                    } else {

                                        iterator.remove();
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_SLIDE)) {
                                    if (bean.getLink().getType().equals("doc")) {
                                        if (bean.getStyle().getView().equals(NewsUtils.VIEW_SLIDEIMG)) {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG;
                                        } else {
                                            bean.itemType = NewsDetail.ItemBean.TYPE_DOC_TITLEIMG;
                                        }
                                    } else {
                                        bean.itemType = NewsDetail.ItemBean.TYPE_SLIDE;
                                    }
                                } else if (bean.getType().equals(NewsUtils.TYPE_PHVIDEO)) {
                                    bean.itemType = NewsDetail.ItemBean.TYPE_PHVIDEO;
                                } else {
                                    iterator.remove();
                                }
                            } catch (Exception e) {
                                iterator.remove();
                                e.printStackTrace();
                            }
                        }
                        return newsDetail.getItem();
                    }
                })
                .compose(mView.<List<NewsDetail.ItemBean>>bindToLife())
                .subscribe(new BaseObserver<List<NewsDetail.ItemBean>>() {
                    @Override
                    public void onSuccess(List<NewsDetail.ItemBean> itemBeans) {
                        if(!action.equals(NewsApi.ACTION_UP)){
                            mView.loadData(itemBeans);
                        }else {
                            mView.loadMoreData(itemBeans);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        Log.i(TAG,"onFail" + e.getMessage().toString());
                        if(!action.equals(NewsApi.ACTION_UP)){
                            mView.loadData(null);
                        }else {
                            mView.loadMoreData(null);
                        }
                    }
                });

    }
}

package com.davy.davy_news.ui.jiandan.presenter;

import android.util.Log;

import com.davy.davy_news.bean.FreshNewsBean;
import com.davy.davy_news.bean.JdDetailBean;
import com.davy.davy_news.net.BaseObserver;
import com.davy.davy_news.net.JianDanApi;
import com.davy.davy_news.net.RxSchedulers;
import com.davy.davy_news.ui.base.BasePresenter;
import com.davy.davy_news.ui.jiandan.contract.JianDanContract;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * author: Davy
 * date: 18/8/15
 */
public class JianDanPresenter extends BasePresenter<JianDanContract.View> implements JianDanContract.Presenter{
    static final String TAG = JianDanPresenter.class.getSimpleName();

    JianDanApi mJianDanApi;

    @Inject
    public JianDanPresenter(JianDanApi jianDanApi){

        this.mJianDanApi = jianDanApi;

    }

    @Override
    public void getData(String type, int page) {
        if(type.equals(JianDanApi.TYPE_FRESH)){
            getFreshNews(page);
        }else{
            getDetailData(type,page);
        }
    }

    @Override
    public void getFreshNews(final int page) {
        mJianDanApi.getFreshNews(page)
                .compose(RxSchedulers.<FreshNewsBean>applySchedulers())
                .compose(mView.<FreshNewsBean>bindToLife())
                .subscribe(new BaseObserver<FreshNewsBean>() {
                    @Override
                    public void onSuccess(FreshNewsBean freshNewsBean) {
                        if(page > 1){
                            mView.loadMoreFreshNews(freshNewsBean);
                        }else{
                            mView.loadFreshNews(freshNewsBean);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });
    }

    @Override
    public void getDetailData(final String type, final int page) {
        mJianDanApi.getJianDanDetails(type,page)
                .compose(RxSchedulers.<JdDetailBean>applySchedulers())
                .compose(mView.<JdDetailBean>bindToLife())
                .map(new Function<JdDetailBean, JdDetailBean>() {
                    @Override
                    public JdDetailBean apply(JdDetailBean jdDetailBean) throws Exception {
                        for(JdDetailBean.CommentsBean bean : jdDetailBean.getComments()){
                            if(bean.getPics()!= null){
                                if(bean.getPics().size() > 1){
                                    bean.itemType = JdDetailBean.CommentsBean.TYPE_MULTIPLE;
                                }else {
                                    bean.itemType = JdDetailBean.CommentsBean.TYPE_SINGLE;
                                }
                            }
                        }
                        return jdDetailBean;
                    }
                })
                .subscribe(new BaseObserver<JdDetailBean>() {
                    @Override
                    public void onSuccess(JdDetailBean jdDetailBean) {
                        if(page > 1){
                            mView.loadMoreDetailData(type,jdDetailBean);
                        }else {
                            mView.loadDetailData(type,jdDetailBean);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        if(page > 1){
                            mView.loadMoreDetailData(type,null);
                        }else {
                            mView.loadDetailData(type,null);
                        }

                        Log.i(TAG,"onFail" + e.getMessage());
                    }
                });
    }
}

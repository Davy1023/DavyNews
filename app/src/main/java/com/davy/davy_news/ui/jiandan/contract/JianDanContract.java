package com.davy.davy_news.ui.jiandan.contract;

import com.davy.davy_news.bean.FreshNewsBean;
import com.davy.davy_news.bean.JdDetailBean;
import com.davy.davy_news.ui.base.BaseContract;

/**
 * author: Davy
 * date: 18/8/15
 */
public interface JianDanContract {

    interface View extends BaseContract.BaseView{

        void loadFreshNews(FreshNewsBean freshNewsBean);

        void loadMoreFreshNews(FreshNewsBean freshNewsBean);

        void loadDetailData(String type,JdDetailBean jdDetailBean);

        void loadMoreDetailData(String type,JdDetailBean jdDetailBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{

        void getData(String type, int page);

        void getFreshNews(int page);

        void getDetailData(String type, int page);
    }
}

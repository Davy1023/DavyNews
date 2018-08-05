package com.davy.davy_news.ui.news.contract;

import com.davy.davy_news.bean.NewsArticleBean;
import com.davy.davy_news.ui.base.BaseContract;

/**
 * author: Davy
 * date: 18/8/4
 */
public interface ArticleReadContract {

    interface View extends BaseContract.BaseView{

        void loadData(NewsArticleBean articleBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{

        void getData(String aid);

    }
}

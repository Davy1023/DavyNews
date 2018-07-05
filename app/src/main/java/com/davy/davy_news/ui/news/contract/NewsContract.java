package com.davy.davy_news.ui.news.contract;

import com.davy.davy_news.bean.Channel;
import com.davy.davy_news.ui.base.BaseContract;

import java.util.List;

/**
 * author: Davy
 * date: 18/7/4
 */
public interface NewsContract {

    interface View extends BaseContract.BaseView{

        void loadData(List<Channel> channels,List<Channel> otherChannels);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        /**
         * 初始化频道
         */

        void getChannel();
    }
}

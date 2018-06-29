package com.davy.davy_news;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

public class DavyNewsApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        BGASwipeBackManager.getInstance().init(this);
        LitePal.initialize(this);
    }
}

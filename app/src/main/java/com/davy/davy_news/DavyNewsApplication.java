package com.davy.davy_news;

import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.component.DaggerApplicationComponent;
import com.davy.davy_news.module.ApplicationModule;
import com.davy.davy_news.module.HttpModule;
import com.davy.davy_news.utils.ContextUtils;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * author: Davy
 * date: 18/7/2
 */

public class DavyNewsApplication extends LitePalApplication {

    private ApplicationComponent mApplicationComponent;
    private static DavyNewsApplication davyNewsApplication;

    public static int width = 0;
    public static int height = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        davyNewsApplication = this;
        BGASwipeBackManager.getInstance().init(this);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();

        LitePal.initialize(this);
        width = ContextUtils.getSreenWidth(DavyNewsApplication.getContext());
        height = ContextUtils.getSreenHeight(DavyNewsApplication.getContext());
    }

    public static DavyNewsApplication getInstance(){

        return davyNewsApplication;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}

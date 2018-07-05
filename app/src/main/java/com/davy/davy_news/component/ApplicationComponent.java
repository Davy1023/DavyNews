package com.davy.davy_news.component;

import com.davy.davy_news.DavyNewsApplication;
import com.davy.davy_news.module.ApplicationModule;
import com.davy.davy_news.module.HttpModule;
import com.davy.davy_news.net.NewsApi;

import dagger.Component;

/**
 * author: Davy
 * date: 18/7/2
 */
@Component(modules = {ApplicationModule.class, HttpModule.class})
public interface ApplicationComponent {

    DavyNewsApplication getApplication();

    NewsApi getNewsApi();
}

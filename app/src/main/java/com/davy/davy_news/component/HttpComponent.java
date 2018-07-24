package com.davy.davy_news.component;

import com.davy.davy_news.ui.DetailFragment;
import com.davy.davy_news.ui.news.NewsFragment;

import dagger.Component;

/**
 * author: Davy
 * date: 18/7/2
 */
@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {

    void inject (NewsFragment newsFragment);
    void inject (DetailFragment detailFragment);
}

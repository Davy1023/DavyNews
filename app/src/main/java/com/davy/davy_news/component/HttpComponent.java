package com.davy.davy_news.component;

import com.davy.davy_news.ui.jiandan.JianDanDetailFragment;
import com.davy.davy_news.ui.news.ArticleReadActivity;
import com.davy.davy_news.ui.news.DetailFragment;
import com.davy.davy_news.ui.news.ImageBrowseActivity;
import com.davy.davy_news.ui.news.NewsFragment;
import com.davy.davy_news.ui.video.DetailsFragment;
import com.davy.davy_news.ui.video.VideosFragment;

import dagger.Component;

/**
 * author: Davy
 * date: 18/7/2
 */
@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {

    void inject (NewsFragment newsFragment);

    void inject (DetailFragment detailFragment);

    void inject (ArticleReadActivity articleReadActivity);

    void inject (ImageBrowseActivity imageBrowseActivity);

    void inject (VideosFragment videosFragment);

    void inject (DetailsFragment detailsFragment);

    void inject (JianDanDetailFragment jianDanDetailFragment);
}

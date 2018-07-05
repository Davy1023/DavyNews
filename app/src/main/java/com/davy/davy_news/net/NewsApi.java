package com.davy.davy_news.net;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * author: Davy
 * date: 18/7/2
 */
public class NewsApi {

    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_DOWN = "down";
    public static final String ACTION_UP = "up";

    @StringDef({ACTION_DEFAULT,ACTION_DOWN,ACTION_UP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Actions{

    }

    private NewsApiService mNewsService;
    public static NewsApi instance;

    public NewsApi(NewsApiService newsApiService){
        this.mNewsService = newsApiService;
    }

    public static NewsApi getInstance(NewsApiService newsApiService){
        if(instance == null){
            instance = new NewsApi(newsApiService);
        }

        return instance;

    }
}

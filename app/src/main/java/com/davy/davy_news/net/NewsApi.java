package com.davy.davy_news.net;

import android.support.annotation.StringDef;

import com.davy.davy_news.bean.NewsDetail;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import io.reactivex.Observable;

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

    /**
     * 获取新闻详情
     *
     * @param id        频道ID值
     * @param action    用户操作方式
     *                  1：下拉 down
     *                  2：上拉 up
     *                  3：默认 default
     * @param pullNum   操作次数 累计
     * @return
     */
    public Observable<List<NewsDetail>> getNewsDetail(String id,@Actions String action,int pullNum){

        return mNewsService.getNewsDetail(id,action,pullNum);
    }
}

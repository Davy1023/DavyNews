package com.davy.davy_news.net;

import com.davy.davy_news.bean.NewsArticleBean;
import com.davy.davy_news.bean.NewsDetail;
import com.davy.davy_news.bean.VideoChannelBean;
import com.davy.davy_news.bean.VideoDetailBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * author: Davy
 * date: 18/7/24
 */
public interface NewsApiService {

    @GET("ClientNews")
    Observable<List<NewsDetail>> getNewsDetail(@Query("id") String id,
                                               @Query("action") String action,
                                               @Query("pullNum") int pullNum

    );

    @GET("api_vampire_article_detail")
    Observable<NewsArticleBean> getNewsArticleSub(@Query("aid") String aid);

    @GET
    Observable<NewsArticleBean> getNewsArticleCmpp(@Url String url,
                                                   @Query("aid") String aid
                                                   );

    @GET("ifengvideoList")
    Observable<List<VideoChannelBean>> getVideoChannel(@Query("page") int page);

    @GET("ifengvideoList")
    Observable<List<VideoDetailBean>> getVideoDetails(@Query("page") int page,
                                                      @Query("listtype") String listtype,
                                                      @Query("typeid") String typeid
                                                      );
}

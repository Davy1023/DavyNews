package com.davy.davy_news.net;

import com.davy.davy_news.bean.NewsDetail;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

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
}

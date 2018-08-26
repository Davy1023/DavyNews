package com.davy.davy_news.net;

import android.support.annotation.StringDef;

import com.davy.davy_news.bean.FreshNewsArticleBean;
import com.davy.davy_news.bean.FreshNewsBean;
import com.davy.davy_news.bean.JdDetailBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.Observable;

/**
 * author: Davy
 * date: 18/8/15
 */
public class JianDanApi {

    public static final String TYPE_FRESH = "get_recent_posts";
    public static final String TYPE_FRESHARTICLE = "get_post";
    public static final String TYPE_BORED = "jandan.get_pic_comments";
    public static final String TYPE_GIRLS = "jandan.get_ooxx_comments";
    public static final String TYPE_Duan = "jandan.get_duan_comments";

    @StringDef({TYPE_FRESH,TYPE_BORED,TYPE_GIRLS,TYPE_Duan})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type{

    }

    public static JianDanApi instance;
    private JianDanApiService mService;
    public static JianDanApi getInstance(JianDanApiService jianDanApiService){
        if(instance == null){
            instance = new JianDanApi(jianDanApiService);
        }

        return instance;
    }

    public JianDanApi(JianDanApiService jianDanApiService){
        this.mService = jianDanApiService;
    }

    /**
     * 获取新鲜事列表
     *
     * @param page
     * @return
     */
    public Observable<FreshNewsBean> getFreshNews(int page){
        return mService.getFreshNews(ApiConstants.sJanDanApi,TYPE_FRESH,
                "url,date,tags,author,title,excerpt,comment_count,comment_status,custom_fields",
                page,"thumb_c,views","1"
                );
    }

    public Observable<FreshNewsArticleBean> getFreshNewsArticle(int id){
        return mService.getFreshNewsArticle(ApiConstants.sJanDanApi,TYPE_FRESHARTICLE,"content,date,modified",id);
    }

    /***
     * 获取 无聊图，妹子图，搞笑段子列表
     * @param type
     * @param page
     * @return
     */
    public Observable<JdDetailBean> getJianDanDetails(@Type String type, int page){
        return mService.getJianDanDetails(ApiConstants.sJanDanApi,type,page);
    }
}

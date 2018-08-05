package com.davy.davy_news.utils;

import com.davy.davy_news.bean.NewsDetail;

/**
 * author: Davy
 * date: 18/7/28
 */
public class NewsUtils {

    //顶部banner新闻
    public static final String TYPE_BANNER = "focus";
    //置顶新闻
    public static final String TYPE_TOP = "top";
    //常规新闻
    public static final String TYPE_List = "list";

    //文章类型
    public static final String TYPE_DOC = "doc";
    //广告类型
    public static final String TYPE_ADVERT = "advert";
    //图片类型
    public static final String TYPE_SLIDE = "slide";
    //视频类型
    public static final String TYPE_PHVIDEO = "phvideo";

    //显示形式单图
    public static final String VIEW_TITLEIMG = "titleimg";
    //显示形式多图
    public static final String VIEW_SLIDEIMG = "slideimg";
    //显示形式多图
    public static final String VIEW_LONGIMG = "longimg";



    public static boolean isBannerNews(NewsDetail detail) {
        return detail.getType().equals(TYPE_BANNER);
    }

    public static boolean isTopNews(NewsDetail detail) {
        return detail.getType().equals(TYPE_TOP);
    }

    public static boolean isListNews(NewsDetail detail) {
        return detail.getType().equals(TYPE_List);
    }

    public boolean isAvertNews(NewsDetail.ItemBean bean) {
        return bean.getType().equals(TYPE_ADVERT);
    }

}

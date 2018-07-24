package com.davy.davy_news.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.davy.davy_news.bean.NewsDetail;

import java.util.List;

/**
 * author: Davy
 * date: 18/7/24
 */
public class NewsDetailAdapter extends BaseMultiItemQuickAdapter<NewsDetail.ItemBean,BaseViewHolder>{

    private Context mContext;

    public NewsDetailAdapter(List<NewsDetail.ItemBean> data, Context context) {
        super(data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsDetail.ItemBean item) {

    }
}

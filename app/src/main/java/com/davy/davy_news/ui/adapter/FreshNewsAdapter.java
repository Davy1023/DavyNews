package com.davy.davy_news.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.davy.davy_news.R;
import com.davy.davy_news.bean.FreshNewsBean;
import com.davy.davy_news.utils.ImageLoaderUtil;

import java.util.List;

/**
 * author: Davy
 * date: 18/8/15
 */
public class FreshNewsAdapter extends BaseQuickAdapter<FreshNewsBean.PostsBean, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {

    private Context mContext;

    public FreshNewsAdapter(Context context, @Nullable List<FreshNewsBean.PostsBean> data){
        super(R.layout.item_freshnews, data);
        this.mContext = context;
    }
    @Override
    protected void convert(BaseViewHolder viewHolder, FreshNewsBean.PostsBean itemBean) {
            viewHolder.setText(R.id.tv_title,itemBean.getTitle());
            viewHolder.setText(R.id.tv_info,itemBean.getAuthor().getName());
            viewHolder.setText(R.id.tv_commnetsize,itemBean.getComment_count() + "评论");
            ImageLoaderUtil.LoadImage(mContext,itemBean.getCustom_fields().getThumb_c().get(0),(ImageView) viewHolder.getView(R.id.iv_logo));

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}

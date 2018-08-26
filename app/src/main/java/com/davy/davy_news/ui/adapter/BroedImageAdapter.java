package com.davy.davy_news.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.davy.davy_news.DavyNewsApplication;
import com.davy.davy_news.R;
import com.davy.davy_news.bean.JdDetailBean;
import com.davy.davy_news.ui.jiandan.ImageBrowseActivity;
import com.davy.davy_news.utils.ContextUtils;
import com.davy.davy_news.utils.DateUtil;
import com.davy.davy_news.utils.ImageLoaderUtil;
import com.davy.davy_news.utils.ShareUtils;
import com.davy.davy_news.widget.MultiImageView;
import com.davy.davy_news.widget.ShowMaxImageView;

import java.util.List;

/**
 * author: Davy
 * date: 18/8/24
 */
public class BroedImageAdapter extends BaseMultiItemQuickAdapter<JdDetailBean.CommentsBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */

    static final String TAG = BroedImageAdapter.class.getSimpleName();
    private Activity mContext;

    public BroedImageAdapter(Activity context,List<JdDetailBean.CommentsBean> data) {
        super(data);
        addItemType(JdDetailBean.CommentsBean.TYPE_MULTIPLE, R.layout.item_jandan_pic);
        addItemType(JdDetailBean.CommentsBean.TYPE_SINGLE,R.layout.item_jandan_pic_single);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, final JdDetailBean.CommentsBean itemBean) {
        viewHolder.setText(R.id.tv_author,itemBean.getComment_author());
        if(!TextUtils.isEmpty(itemBean.getComment_agent())){
            if(itemBean.getComment_agent().contains("Android")){
                viewHolder.setText(R.id.tv_from,"来自 Android 客户端");
                viewHolder.setVisible(R.id.tv_from,true);
            }else {
                viewHolder.setVisible(R.id.tv_from,false);
            }
        }else{
            viewHolder.setVisible(R.id.tv_from,false);
        }

        viewHolder.setText(R.id.tv_time, DateUtil.getTimestampString(DateUtil.string2Date(itemBean.getComment_date(),"yyyy-MM-dd HH:mm:ss")));

        if(TextUtils.isEmpty(itemBean.getText_content())){
            viewHolder.setVisible(R.id.tv_content,false);
        }else{
            viewHolder.setVisible(R.id.tv_content,true);
            String content = itemBean.getText_content().replace(" ","").replace("\r","").replace("\n","");
            viewHolder.setText(R.id.tv_content,content);

            Log.i(TAG,"author:" + itemBean.getComment_author() + "content:" + itemBean.getText_content());

        }

        viewHolder.setVisible(R.id.img_gif,itemBean.getPics().get(0).contains("gif"));
        viewHolder.setVisible(R.id.progress,itemBean.getPics().get(0).contains("gif"));
        viewHolder.setText(R.id.tv_like,itemBean.getVote_negative());
        viewHolder.setText(R.id.tv_unlike,itemBean.getVote_positive());
        viewHolder.setText(R.id.tv_comment_count,itemBean.getSub_comment_count());
        viewHolder.addOnClickListener(R.id.img_share);

        switch (viewHolder.getItemViewType()){
            case JdDetailBean.CommentsBean.TYPE_MULTIPLE:
                    MultiImageView multiImageView = viewHolder.getView(R.id.img);
                    viewHolder.setVisible(R.id.img_gif,false);
                    multiImageView.setList(itemBean.getPics());
                    multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            String[] imageUrls = new String[itemBean.getPics().size()];
                            for(int i = 0; i < itemBean.getPics().size(); i++){
                                imageUrls[i] = itemBean.getPics().get(i);
                            }
                            ImageBrowseActivity.launch(mContext,imageUrls,position);
                        }
                    });
                    viewHolder.getView(R.id.img_share).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ShareUtils.shareImage(mContext,itemBean.getPics().get(0));
                        }
                    });
                    break;

            case JdDetailBean.CommentsBean.TYPE_SINGLE:
                ShowMaxImageView showMaxImageView = viewHolder.getView(R.id.img);
               showMaxImageView.getLayoutParams().height = ContextUtils.dip2px(DavyNewsApplication.getContext(),250);

               showMaxImageView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       String[] imageUrls = new String[itemBean.getPics().size()];
                       imageUrls[0] = itemBean.getPics().get(0);
                       ImageBrowseActivity.launch(mContext,imageUrls,0);
                   }
               });
                ImageLoaderUtil.LoadImage(mContext,itemBean.getPics().get(0),
                        new DrawableImageViewTarget((ImageView) viewHolder.getView(R.id.img)){
                            @Override
                            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                                int width =ContextUtils.getSreenWidth(DavyNewsApplication.getContext());
                                int height = ContextUtils.getSreenHeight(DavyNewsApplication.getContext());
                                float sal = height / width;
                                int actualHeight = (int) Math.ceil(sal * resource.getIntrinsicWidth());
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,actualHeight);
                                viewHolder.getView(R.id.img).setLayoutParams(params);
                                viewHolder.setVisible(R.id.img_gif,false);
                            }
                        });
                viewHolder.getView(R.id.img_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShareUtils.shareText(mContext,"http://jandan.net/pic/");
                    }
                });
                break;
        }

    }
}

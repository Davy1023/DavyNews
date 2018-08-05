package com.davy.davy_news.ui.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davy.davy_news.R;
import com.davy.davy_news.bean.NewsArticleBean;
import com.davy.davy_news.bean.NewsDetail;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.component.DaggerHttpComponent;
import com.davy.davy_news.net.ApiConstants;
import com.davy.davy_news.ui.base.BaseActivity;
import com.davy.davy_news.ui.news.Presenter.ArticleReadPresenter;
import com.davy.davy_news.ui.news.contract.ArticleReadContract;
import com.davy.davy_news.utils.ImageLoaderUtil;
import com.davy.davy_news.widget.DavyViewPager;
import com.davy.davy_news.widget.MyScrollView;
import com.davy.davy_news.widget.SwipeBackLayout;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: Davy
 * date: 18/8/5
 */
public class ImageBrowseActivity extends BaseActivity<ArticleReadPresenter> implements ArticleReadContract.View {

    static final String TAG = ImageBrowseActivity.class.getSimpleName();

    private static final String AID = "aid";
    private static final String ISCMPP = "isCmpp";
    private boolean isShow = true;
    private NewsArticleBean newsArticleBean;

    @BindView(R.id.view_pager)
    DavyViewPager mViewPager;
    @BindView(R.id.swipe_layout)
    SwipeBackLayout mSwipeLayout;
    @BindView(R.id.btn_titlebar_left)
    ImageView mTitlebarLeft;
    @BindView(R.id.tv_titlebar_name)
    TextView mTitlebarName;
    @BindView(R.id.rl_top)
    RelativeLayout mRlTop;
    @BindView(R.id.tv_info)
    TextView mInfo;
    @BindView(R.id.scrollview)
    MyScrollView mScrollview;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;

    public static void launch(Activity context, NewsDetail.ItemBean itemBean) {
        Intent intent = new Intent(context,ImageBrowseActivity.class);
        if(itemBean.getId().contains(ApiConstants.sGetNewsArticleCmppApi)|| itemBean.getDocumentId().startsWith("cmpp")){
            intent.putExtra(ISCMPP,true);
        }else {
            intent.putExtra(ISCMPP,false);
        }
        intent.putExtra(AID,itemBean.getDocumentId());
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_imagebrowse;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(applicationComponent)
                .build()
                .inject(this);
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        mRelativeLayout.getBackground().setAlpha(255);
        mSwipeBackHelper.setSwipeBackEnable(true);
        mSwipeLayout.setDragDirectMode(SwipeBackLayout.DragDirectMode.VERTICAL);
        mSwipeLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                mRelativeLayout.getBackground().setAlpha((int) (255 - Math.ceil(255 * fractionAnchor)));
                DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                df.setMaximumFractionDigits(1);
                df.setRoundingMode(RoundingMode.HALF_UP);
                String dd = df.format(fractionAnchor);
                double alpha = 1 - (Float.valueOf(dd) + 0.8);
                if(fractionAnchor == 0 && isShow){
                    mScrollview.setAlpha(1f);
                    mRlTop.setAlpha(1f);
                    mScrollview.setVisibility(View.VISIBLE);
                    mRlTop.setVisibility(View.VISIBLE);
                }else {
                    if(alpha == 0){
                        mRlTop.setVisibility(View.GONE);
                        mScrollview.setVisibility(View.GONE);
                        mScrollview.setAlpha(1f);
                        mRlTop.setAlpha(1F);
                    }else {
                        if(mRlTop.getVisibility() != View.GONE){
                            mRlTop.setAlpha((float) alpha);
                            mScrollview.setAlpha((float) alpha);
                        }
                    }
                }
            }
        });

        mScrollview.getBackground().mutate().setAlpha(100);
        mRlTop.getBackground().mutate().setAlpha(100);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            mInfo.setText((position + 1) + "/" + newsArticleBean.getBody().getSlides().size() + " " + newsArticleBean.getBody().getSlides().get(position).getDescription());
            if(position == 0){
                mSwipeBackHelper.setSwipeBackEnable(true);
            }else {
                mSwipeBackHelper.setSwipeBackEnable(false);
            }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void initData() {
        if(getIntent() == null) return;
        String aid = getIntent().getStringExtra(AID);
        mPresenter.getData(aid);
    }

    @Override
    public void loadData(NewsArticleBean articleBean) {
        try {
            this.newsArticleBean = articleBean;
            mInfo.setText("1 / " + articleBean.getBody().getSlides().size() + " " + articleBean.getBody().getSlides().get(0).getDescription());
            mViewPager.setAdapter(new ViewPagerAdapter(articleBean.getBody().getSlides()));
            mTitlebarName.setText(articleBean.getBody().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRetry() {
        initData();
    }

    @OnClick(R.id.btn_titlebar_left)
    public void onViewClicked(){
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private List<NewsArticleBean.BodyBean.SlidesBean> slidesBeanList;
        private PhotoView mPhotoView;
        private ProgressBar mProgressBar;

        public ViewPagerAdapter(List<NewsArticleBean.BodyBean.SlidesBean> slides) {
            this.slidesBeanList = slides;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ImageBrowseActivity.this).inflate(R.layout.loadimage,null);
            mPhotoView = view.findViewById(R.id.photoview);
            mProgressBar = view.findViewById(R.id.loading);
            mPhotoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    if(isShow){
                        isShow = false;
                        setView(mRlTop,false);
                        setView(mScrollview,false);
                    }else {
                        isShow = true;
                        setView(mRlTop,true);
                        setView(mScrollview,true);
                    }
                }
            });
            mProgressBar.setVisibility(View.GONE);

            ImageLoaderUtil.LoadImage(ImageBrowseActivity.this,slidesBeanList.get(position).getImage(),new DrawableImageViewTarget(mPhotoView){
                @Override
                public void setDrawable(Drawable drawable) {
                    super.setDrawable(drawable);
                }

                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    super.onLoadCleared(placeholder);
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    super.onResourceReady(resource, transition);
                    mProgressBar.setVisibility(View.GONE);
                }
            });
            container.addView(view);
            return view;
        }



        @Override
        public int getCount() {
            return slidesBeanList == null ? 0 : slidesBeanList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void setView(final View view, final boolean isShow) {
        AlphaAnimation alphaAnimation;
        if(isShow){
            alphaAnimation = new AlphaAnimation(0,1);
        }else {
            alphaAnimation = new AlphaAnimation(1,0);
        }
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(500);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}

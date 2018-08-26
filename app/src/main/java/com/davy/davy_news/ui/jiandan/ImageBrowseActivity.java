package com.davy.davy_news.ui.jiandan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davy.davy_news.R;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.ui.base.BaseActivity;
import com.davy.davy_news.utils.ImageLoaderUtil;
import com.davy.davy_news.widget.DavyViewPager;
import com.davy.davy_news.widget.SwipeBackLayout;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;

/**
 * author: Davy
 * date: 18/8/25
 */
public class ImageBrowseActivity extends BaseActivity {

    static final String TAG = ImageBrowseActivity.class.getSimpleName();
    @BindView(R.id.view_pager)
    DavyViewPager mViewPager;
    @BindView(R.id.swipe_layout)
    SwipeBackLayout mSwipeLayout;
    @BindView(R.id.page_text)
    TextView mImageText;
    @BindView(R.id.rl_imagebrowse)
    RelativeLayout mRlImagebrowse;

    private int selectedIndex;
    private String[] imageUrls;

    public static void launch(Activity context, String[] urls, int selectedIndex){
        Intent intent = new Intent(context,ImageBrowseActivity.class);
        intent.putExtra("urls",urls);
        intent.putExtra("selectedIndex",selectedIndex);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
    @Override
    public int getContentLayout() {
        return R.layout.activity_image_browse;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        mRlImagebrowse.getBackground().setAlpha(255);
        mSwipeLayout.setDragDirectMode(SwipeBackLayout.DragDirectMode.VERTICAL);
        mSwipeLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                mRlImagebrowse.getBackground().setAlpha((int) (255 - Math.ceil(255 * fractionAnchor)));
            }
        });
    }

    @Override
    public void initData() {
        if(getIntent().getExtras() == null) return;
        imageUrls = getIntent().getExtras().getStringArray("urls");
        selectedIndex = getIntent().getExtras().getInt("selectedIndex");
        if(imageUrls == null) return;

        mViewPager.setAdapter(new ViewPagerAdapter(imageUrls));
        mViewPager.setCurrentItem(selectedIndex);
        if(selectedIndex == 0){
            mSwipeBackHelper.setSwipeBackEnable(true);
        }else {
            mSwipeBackHelper.setSwipeBackEnable(false);
        }

        if(imageUrls.length > 1){
            mImageText.setText(selectedIndex + 1 + " / " + imageUrls.length);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mImageText.setText(position + 1 + " / " + imageUrls.length);
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

    }

    @Override
    public void onRetry() {

    }

    private class  ViewPagerAdapter extends PagerAdapter{

        private String[] mImageUrls;
        private PhotoView mPhotoView;
        private ProgressBar mProgressBar;

        private ViewPagerAdapter(String[] imageUrls){
            this.mImageUrls = imageUrls;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ImageBrowseActivity.this).inflate(R.layout.loadimage, null);
            mPhotoView = view.findViewById(R.id.photoview);
            mProgressBar = view.findViewById(R.id.loading);
            mProgressBar.setVisibility(View.GONE);
            mPhotoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    finish();
                }
            });
            ImageLoaderUtil.LoadImage(ImageBrowseActivity.this,imageUrls[position],new DrawableImageViewTarget(mPhotoView){
                @Override
                public void setDrawable(Drawable drawable) {
                    super.setDrawable(drawable);
                    Log.i(TAG,"setDrawable:");
                }

                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    Log.i(TAG,"onLoadStarted:");
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    Log.i(TAG,"onLoadFailed:");
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    super.onLoadCleared(placeholder);
                    Log.i(TAG,"onLoadCleared:");
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    super.onResourceReady(resource, transition);
                    Log.i(TAG,"onResourceReady:");
                    mProgressBar.setVisibility(View.GONE);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mImageUrls.length > 0 ? mImageUrls.length : 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}

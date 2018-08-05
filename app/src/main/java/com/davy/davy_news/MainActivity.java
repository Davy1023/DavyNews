package com.davy.davy_news;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.ui.base.BaseActivity;
import com.davy.davy_news.ui.base.SupportFragment;
import com.davy.davy_news.ui.jiandan.JanDanFragment;
import com.davy.davy_news.ui.news.NewsFragment;
import com.davy.davy_news.ui.personal.PersonalFragment;
import com.davy.davy_news.ui.video.VideosFragment;
import com.davy.davy_news.utils.StatusBarUtil;
import com.davy.davy_news.widget.BottomBar;
import com.davy.davy_news.widget.BottomBarTab;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.contentContainer)
    FrameLayout mContentContainer;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;

    private SupportFragment[] mFragments = new SupportFragment[4];

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
        if(savedInstanceState == null){
            mFragments[0] = NewsFragment.newInstance();
            mFragments[1] = VideosFragment.newInstance();
            mFragments[2] = JanDanFragment.newInstance();
            mFragments[3] = PersonalFragment.newInstance();

            getSupportDelegate().loadMultipleRootFragment(R.id.contentContainer,0,
                    mFragments[0],
                    mFragments[1],
                    mFragments[2],
                    mFragments[3]);
        }else{

            mFragments[0] = findFragment(NewsFragment.class);
            mFragments[1] = findFragment(VideosFragment.class);
            mFragments[2] = findFragment(JanDanFragment.class);
            mFragments[3] = findFragment(PersonalFragment.class);
        }

        mBottomBar.addItem(new BottomBarTab(this,R.mipmap.ic_news,"新闻"))
                .addItem(new BottomBarTab(this,R.mipmap.ic_video,"视频"))
                .addItem(new BottomBarTab(this,R.mipmap.ic_jiandan,"煎蛋"))
                .addItem(new BottomBarTab(this,R.mipmap.ic_my,"我的"));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                getSupportDelegate().showHideFragment(mFragments[position],mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }



    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

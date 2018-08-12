package com.davy.davy_news.ui.video;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davy.davy_news.R;
import com.davy.davy_news.bean.VideoChannelBean;
import com.davy.davy_news.bean.VideoDetailBean;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.component.DaggerHttpComponent;
import com.davy.davy_news.ui.adapter.VideoPagerAdapter;
import com.davy.davy_news.ui.base.BaseFragment;
import com.davy.davy_news.ui.video.contract.VideoContract;
import com.davy.davy_news.ui.video.presenter.VideoPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: Davy
 * date: 18/7/2
 */
public class VideosFragment extends BaseFragment<VideoPresenter> implements VideoContract.View {

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private VideoPagerAdapter mVideoPagerAdapter;

    public static VideosFragment newInstance() {
        Bundle args = new Bundle();
        VideosFragment fragment = new VideosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_video;
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

    }

    @Override
    public void initData() {
        mPresenter.getVideoChannel();
    }

    @Override
    public void loadVideoChannel(List<VideoChannelBean> videoChannelBeans) {
        mVideoPagerAdapter = new VideoPagerAdapter(getChildFragmentManager(),videoChannelBeans.get(0));
        mViewPager.setAdapter(mVideoPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(0,false);
        mTabLayout.setupWithViewPager(mViewPager,true);
    }

    @Override
    public void loadVideoDetails(List<VideoDetailBean> videoDetailBeans) {

    }

    @Override
    public void loadMoreVideoDetails(List<VideoDetailBean> videoDetailBeans) {

    }

}

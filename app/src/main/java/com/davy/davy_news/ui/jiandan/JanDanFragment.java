package com.davy.davy_news.ui.jiandan;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.davy.davy_news.R;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.net.JianDanApi;
import com.davy.davy_news.ui.adapter.BroedImageAdapter;
import com.davy.davy_news.ui.adapter.FreshNewsAdapter;
import com.davy.davy_news.ui.adapter.JokesAdapter;
import com.davy.davy_news.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: Davy
 * date: 18/7/2
 */
public class JanDanFragment extends BaseFragment {

    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    private JianDanPagerAdapter mJianDanPagerAdapter;

    public static JanDanFragment newInstance() {
        Bundle args = new Bundle();
        JanDanFragment fragment = new JanDanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_jiandan;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        List<String> titles = new ArrayList<>();
        titles.add("新鲜事");
        titles.add("无聊图");
        titles.add("妹子图");
        titles.add("搞笑段子");

        mJianDanPagerAdapter = new JianDanPagerAdapter(getChildFragmentManager(),titles);
        mViewpager.setAdapter(mJianDanPagerAdapter);
        mViewpager.setOffscreenPageLimit(1);
        mViewpager.setCurrentItem(0,false);
        mTablayout.setupWithViewPager(mViewpager,true);

    }

    public class  JianDanPagerAdapter extends FragmentStatePagerAdapter{
        private List<String> mTitle;

        public JianDanPagerAdapter(FragmentManager fm, List<String> titles){
            super(fm);
            this.mTitle = titles;
        }

        @Override
        public BaseFragment getItem(int position) {
            switch (position){
                case 0:
                    return JianDanDetailFragment.newInstance(JianDanApi.TYPE_FRESH,new FreshNewsAdapter(getActivity(),null));
                case 1:
                    return JianDanDetailFragment.newInstance(JianDanApi.TYPE_BORED,new BroedImageAdapter(getActivity(),null));
                case 2:
                    return JianDanDetailFragment.newInstance(JianDanApi.TYPE_GIRLS,new BroedImageAdapter(getActivity(),null));
                case 3:
                    return JianDanDetailFragment.newInstance(JianDanApi.TYPE_Duan,new JokesAdapter(null));
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTitle.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}

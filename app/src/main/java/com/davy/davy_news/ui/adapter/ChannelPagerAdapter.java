package com.davy.davy_news.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.davy.davy_news.bean.Channel;
import com.davy.davy_news.ui.base.BaseFragment;
import com.davy.davy_news.ui.news.DetailFragment;

import java.util.List;

/**
 * author: Davy
 * date: 18/7/14
 */
public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private List<Channel> mChannel;

    public ChannelPagerAdapter(FragmentManager fm, List<Channel> channels) {
        super(fm);
        this.mChannel = channels;
    }

    public void updateChannel(List<Channel> channels){
        this.mChannel = channels;
        notifyDataSetChanged();
    }

    @Override
    public BaseFragment getItem(int position) {
        return DetailFragment.newsInstance(mChannel.get(position).getChannelId(),position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannel.get(position).getChannelName();
    }

    @Override
    public int getCount() {
        return mChannel != null ? mChannel.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

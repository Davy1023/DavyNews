package com.davy.davy_news.ui.video;

import android.os.Bundle;
import android.view.View;

import com.davy.davy_news.R;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.ui.base.BaseFragment;

/**
 * author: Davy
 * date: 18/7/2
 */
public class VideosFragment extends BaseFragment{

    public static VideosFragment newInstance(){
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

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }
}

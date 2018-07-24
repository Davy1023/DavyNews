package com.davy.davy_news.ui.jiandan;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.ui.base.BaseFragment;

/**
 * author: Davy
 * date: 18/7/2
 */
public class JanDanFragment extends BaseFragment{

    public static JanDanFragment newInstance(){
        Bundle args = new Bundle();
        JanDanFragment fragment = new JanDanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return 0;
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

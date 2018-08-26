package com.davy.davy_news.ui.personal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.davy.davy_news.R;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.ui.base.BaseFragment;

import butterknife.OnClick;

/**
 * author: Davy
 * date: 18/7/2
 */
public class PersonalFragment extends BaseFragment{

    public static PersonalFragment newInstance(){
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_personal;
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

    @OnClick(R.id.tv_github)
    public void onViewClicked(){
        Uri webUrl = Uri.parse("https://github.com/Davy1023");
        Intent intent = new Intent(Intent.ACTION_VIEW,webUrl);
        getActivity().startActivity(intent);
    }
}

package com.davy.davy_news.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_news.R;
import com.davy.davy_news.bean.NewsDetail;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.component.DaggerHttpComponent;
import com.davy.davy_news.net.NewsApi;
import com.davy.davy_news.ui.adapter.NewsDetailAdapter;
import com.davy.davy_news.ui.base.BaseFragment;
import com.davy.davy_news.ui.news.Presenter.DetailPersenter;
import com.davy.davy_news.ui.news.contract.DetailContract;
import com.davy.davy_news.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * author: Davy
 * date: 18/7/24
 */
public class DetailFragment extends BaseFragment<DetailPersenter> implements DetailContract.View {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout rlTopToast;

    private String mNewId;
    private int mPosition;
    private List<NewsDetail.ItemBean> mBeanList;
    private List<NewsDetail.ItemBean> mBannerList;
    private int upPullNum = 1;
    private int downUpNum = 1;
    private boolean isRemoveHeaderView = false;
    private NewsDetailAdapter mNewsDetailAdapter;

    public static DetailFragment newsInstance(String newId, int position) {

        Bundle args = new Bundle();
        args.putString("newId", newId);
        args.putInt("position", position);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_detail;
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
            mPtrFrameLayout.disableWhenHorizontalMove(true);
            mPtrFrameLayout.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame,mRecyclerView,header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                        isRemoveHeaderView = true;
                        mPresenter.getData(mNewId,NewsApi.ACTION_DOWN,downUpNum);
                }
            });

            mBeanList = new ArrayList<>();
            mBannerList = new ArrayList<>();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mNewsDetailAdapter = new NewsDetailAdapter(mBeanList,getActivity()));
            mNewsDetailAdapter.setEnableLoadMore(true);
            mNewsDetailAdapter.setLoadMoreView(new CustomLoadMoreView());
            mNewsDetailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            mNewsDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {

                    mPresenter.getData(mNewId,NewsApi.ACTION_UP,upPullNum);
                }
            },mRecyclerView);
    }

    @Override
    public void initData() {
        if(getArguments() == null) return;
        mNewId = getArguments().getString("newId");
        mPosition = getArguments().getInt("position");
        mPresenter.getData(mNewId, NewsApi.ACTION_DEFAULT,1);
    }

    @Override
    public void loadBannerData(NewsDetail newsDetail) {

    }

    @Override
    public void loadTopNewsData(NewsDetail newsDetail) {

    }

    @Override
    public void loadData(List<NewsDetail.ItemBean> itemBeanList) {

    }

    @Override
    public void loadMoreData(List<NewsDetail.ItemBean> itemBeanList) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}

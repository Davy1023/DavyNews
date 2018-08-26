package com.davy.davy_news.ui.jiandan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.davy.davy_news.R;
import com.davy.davy_news.bean.FreshNewsBean;
import com.davy.davy_news.bean.JdDetailBean;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.component.DaggerHttpComponent;
import com.davy.davy_news.net.JianDanApi;
import com.davy.davy_news.ui.base.BaseFragment;
import com.davy.davy_news.ui.jiandan.contract.JianDanContract;
import com.davy.davy_news.ui.jiandan.presenter.JianDanPresenter;
import com.davy.davy_news.widget.CustomLoadMoreView;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * author: Davy
 * date: 18/8/15
 */
@SuppressLint("ValidFragment")
public class JianDanDetailFragment extends BaseFragment<JianDanPresenter> implements JianDanContract.View {
    public static final String TYPE = "type";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;

    private BaseQuickAdapter mAdapter;
    private String type;
    private int pageNum = 1;

    public JianDanDetailFragment(BaseQuickAdapter baseQuickAdapter) {
        this.mAdapter = baseQuickAdapter;
    }

    public static JianDanDetailFragment newInstance(String type, BaseQuickAdapter baseQuickAdapter){
        Bundle args = new Bundle();
        args.putCharSequence(TYPE,type);
        JianDanDetailFragment fragment = new JianDanDetailFragment(baseQuickAdapter);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getContentLayout() {
        return R.layout.fragment_jd_detail;
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
                pageNum = 1;
                mPresenter.getData(type,pageNum);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setPreLoadNumber(1);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getData(type,pageNum);
            }
        },mRecyclerView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(type.equals(JianDanApi.TYPE_FRESH)){
                    ReadActivity.launch(getActivity(), (FreshNewsBean.PostsBean) adapter.getItem(position));
                }
            }
        });
    }

    @Override
    public void initData() {
        if(getArguments() == null) return;
        type = getArguments().getString(TYPE);
        mPresenter.getData(type,pageNum);
    }

    @Override
    public void onRetry() {
        initData();
    }

    @Override
    public void loadFreshNews(FreshNewsBean freshNewsBean) {
        if(freshNewsBean == null){
            mPtrFrameLayout.refreshComplete();
            showFaild();
        }else{
            pageNum++;
            mAdapter.setNewData(freshNewsBean.getPosts());
            mPtrFrameLayout.refreshComplete();
            showSuccess();
        }
    }

    @Override
    public void loadMoreFreshNews(FreshNewsBean freshNewsBean) {
        if(freshNewsBean == null){
            mAdapter.loadMoreFail();
        }else{
            pageNum++;
            mAdapter.addData(freshNewsBean.getPosts());
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void loadDetailData (String type,JdDetailBean jdDetailBean) {
        if(jdDetailBean == null){
            mPtrFrameLayout.refreshComplete();
            showFaild();
        }else{
            pageNum++;
            mAdapter.setNewData(jdDetailBean.getComments());
            mPtrFrameLayout.refreshComplete();
            showSuccess();
        }
    }

    @Override
    public void loadMoreDetailData(String type,JdDetailBean jdDetailBean) {
        if(jdDetailBean == null){
            mAdapter.loadMoreFail();
        }else {
            pageNum++;
            mAdapter.addData(jdDetailBean.getComments());
            mAdapter.loadMoreComplete();
        }
    }

}

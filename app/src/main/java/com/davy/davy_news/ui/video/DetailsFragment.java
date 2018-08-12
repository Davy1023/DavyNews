package com.davy.davy_news.ui.video;

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
import com.davy.davy_news.bean.VideoChannelBean;
import com.davy.davy_news.bean.VideoDetailBean;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.component.DaggerHttpComponent;
import com.davy.davy_news.ui.adapter.VideoDetailAdapter;
import com.davy.davy_news.ui.base.BaseFragment;
import com.davy.davy_news.ui.video.contract.VideoContract;
import com.davy.davy_news.ui.video.presenter.VideoPresenter;
import com.davy.davy_news.widget.CustomLoadMoreView;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * author: Davy
 * date: 18/8/12
 */
public class DetailsFragment extends BaseFragment<VideoPresenter> implements VideoContract.View {

    public static final String TYPEID = "typeId";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mPtrFrameLayout)
    PtrClassicFrameLayout mPtrFrameLayout;
    @BindView(R.id.tv_toast)
    TextView mToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mRlTopToast;

    private int pageNum = 1;
    private String typeId;
    private VideoDetailBean videoDetailBean;
    private VideoDetailAdapter videoDetailAdapter;

    public static DetailsFragment newInstance(String typeId){
        Bundle args = new Bundle();
        args.putCharSequence(TYPEID,typeId);
        DetailsFragment fragment = new DetailsFragment();
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
                pageNum = 1;
                if(mPresenter != null){
                    mPresenter.getVideoDetails(pageNum,"list",typeId);
                }
            }
        });

        videoDetailBean = new VideoDetailBean();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(videoDetailAdapter = new VideoDetailAdapter(getActivity(),R.layout.item_detail_video,videoDetailBean.getItem()));
        videoDetailAdapter.setEnableLoadMore(true);
        videoDetailAdapter.setLoadMoreView(new CustomLoadMoreView());
        videoDetailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        videoDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getVideoDetails(pageNum,"list",typeId);
            }
        },mRecyclerView);

    }

    @Override
    public void initData() {
        if(getArguments() == null) return;
        typeId = getArguments().getString(TYPEID);
        if(mPresenter != null){
            mPresenter.getVideoDetails(pageNum,"list",typeId);
        }
    }

    @Override
    public void onRetry() {
        initData();
    }

    @Override
    public void loadVideoChannel(List<VideoChannelBean> videoChannelBeans) {

    }

    @Override
    public void loadVideoDetails(List<VideoDetailBean> videoDetailBeans) {
        if(videoDetailBeans == null){
            showFaild();
            return;
        }

        pageNum++;
        videoDetailAdapter.setNewData(videoDetailBeans.get(0).getItem());
        mPtrFrameLayout.refreshComplete();
        showSuccess();
    }

    @Override
    public void loadMoreVideoDetails(List<VideoDetailBean> videoDetailBeans) {
        if(videoDetailBeans == null){
            videoDetailAdapter.loadMoreEnd();
            return;
        }
        pageNum++;
        videoDetailAdapter.addData(videoDetailBeans.get(0).getItem());
        videoDetailAdapter.loadMoreComplete();
    }

}

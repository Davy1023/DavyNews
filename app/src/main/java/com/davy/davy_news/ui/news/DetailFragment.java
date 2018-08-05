package com.davy.davy_news.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.davy.davy_news.DavyNewsApplication;
import com.davy.davy_news.R;
import com.davy.davy_news.bean.NewsDetail;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.component.DaggerHttpComponent;
import com.davy.davy_news.net.NewsApi;
import com.davy.davy_news.ui.adapter.NewsDetailAdapter;
import com.davy.davy_news.ui.base.BaseFragment;
import com.davy.davy_news.ui.news.Presenter.DetailPersenter;
import com.davy.davy_news.ui.news.contract.DetailContract;
import com.davy.davy_news.utils.ContextUtils;
import com.davy.davy_news.utils.ImageLoaderUtil;
import com.davy.davy_news.utils.NewsUtils;
import com.davy.davy_news.widget.CustomLoadMoreView;
import com.davy.davy_news.widget.NewsDelPop;
import com.flyco.animation.SlideEnter.SlideRightEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * author: Davy
 * date: 18/7/24
 */
public class DetailFragment extends BaseFragment<DetailPersenter> implements DetailContract.View {

    static final String TAG = DetailFragment.class.getSimpleName();

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mTopToast;
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;

    private View mBannerView;
    private Banner mBanner;
    private NewsDelPop mNewDelPop;
    private String mNewId;
    private int mPosition;
    private List<NewsDetail.ItemBean> mBeanList;
    private List<NewsDetail.ItemBean> mBannerList;
    private int downPullNum = 1;
    private int upPullNum = 1;
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
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                isRemoveHeaderView = true;
                mPresenter.getData(mNewId, NewsApi.ACTION_DOWN, upPullNum);
            }
        });

        mBeanList = new ArrayList<>();
        mBannerList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsDetailAdapter = new NewsDetailAdapter(mBeanList, getActivity()));
        mNewsDetailAdapter.setEnableLoadMore(true);
        mNewsDetailAdapter.setLoadMoreView(new CustomLoadMoreView());
        mNewsDetailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mNewsDetailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                mPresenter.getData(mNewId, NewsApi.ACTION_UP, downPullNum);
            }
        }, mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) adapter.getItem(position);
                toRead(itemBean);
            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NewsDetail.ItemBean itemBean = (NewsDetail.ItemBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.iv_close:
                        view.getHeight();
                        int[] location = new int[2];
                        view.getLocationInWindow(location);
                        Log.i(TAG, "点击的item的高度:" + view.getHeight() + "x值：" + location[0] + "y值：" + location[1]);
                        if (itemBean.getStyle() == null) return;
                        if (ContextUtils.getSreenWidth(DavyNewsApplication.getContext()) - 50 - location[0] < ContextUtils.dip2px(DavyNewsApplication.getContext(), 80)) {
                            mNewDelPop
                                    .anchorView(view)
                                    .gravity(Gravity.TOP)
                                    .setBackReason(itemBean.getStyle().getBackreason(), true, position)
                                    .show();
                        } else {
                            mNewDelPop
                                    .anchorView(view)
                                    .gravity(Gravity.BOTTOM)
                                    .setBackReason(itemBean.getStyle().getBackreason(), false, position)
                                    .show();
                        }
                        break;
                }
            }
        });

        mBannerView = getView().inflate(getActivity(), R.layout.news_detail_headerview, null);
        mBanner = (Banner) mBannerView.findViewById(R.id.banner);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        //Glide 加载图片简单用法
                        ImageLoaderUtil.LoadImage(getActivity(), path, imageView);
                    }
                })
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mBannerList.size() < 1) return;
                bannerToRead(mBannerList.get(position));
            }
        });

        mNewDelPop = new NewsDelPop(getActivity())
                .alignCenter(false)
                .widthScale(0.95f)
                .showAnim(new SlideRightEnter())
                .dismissAnim(new SlideRightExit())
                .offset(-100, 0)
                .dimEnabled(true);
        mNewDelPop.setClickListener(new NewsDelPop.onClickListener() {
            @Override
            public void onClick(int position) {
                mNewDelPop.dismiss();
                mNewsDetailAdapter.remove(position);
                showToast(0, false);
            }
        });
    }


    @Override
    public void initData() {
        if (getArguments() == null) return;
        mNewId = getArguments().getString("newId");
        mPosition = getArguments().getInt("position");
        mPresenter.getData(mNewId, NewsApi.ACTION_DEFAULT, 1);
    }

    @Override
    public void onRetry() {
        initData();
    }

    @Override
    public void loadBannerData(NewsDetail newsDetail) {

        Log.i(TAG, "loadBannerData: " + newsDetail.toString());
        List<String> mTitleList = new ArrayList<>();
        List<String> mUrlList = new ArrayList<>();
        mBannerList.clear();
        for (NewsDetail.ItemBean bean : newsDetail.getItem()) {
            if (!TextUtils.isEmpty(bean.getThumbnail())) {
                mTitleList.add(bean.getTitle());
                mBannerList.add(bean);
                mUrlList.add(bean.getThumbnail());
            }
        }
        if (mUrlList.size() > 0) {
            mBanner.setImages(mUrlList);
            mBanner.setBannerTitles(mTitleList);
            mBanner.start();
            if (mNewsDetailAdapter.getHeaderLayoutCount() < 1) {
                mNewsDetailAdapter.addHeaderView(mBannerView);
            }
        }
    }

    @Override
    public void loadTopNewsData(NewsDetail newsDetail) {
        Log.i(TAG, "loadTopNewsData: " + newsDetail.toString());
    }

    @Override
    public void loadData(List<NewsDetail.ItemBean> itemBeanList) {
        if (itemBeanList == null || itemBeanList.size() == 0) {
            showFaild();
            mPtrFrameLayout.refreshComplete();
        } else {
            downPullNum++;
            if (isRemoveHeaderView) {
                mNewsDetailAdapter.removeAllHeaderView();
            }
            mNewsDetailAdapter.setNewData(itemBeanList);
            showToast(itemBeanList.size(), true);
            mPtrFrameLayout.refreshComplete();
            showSuccess();
            Log.i(TAG, "loadData: " + itemBeanList.toString());
        }
    }

    @Override
    public void loadMoreData(List<NewsDetail.ItemBean> itemBeanList) {
        if (itemBeanList == null || itemBeanList.size() == 0) {
            mNewsDetailAdapter.loadMoreFail();
        } else {
            upPullNum++;
            mNewsDetailAdapter.addData(itemBeanList);
            mNewsDetailAdapter.loadMoreComplete();
            Log.i(TAG, "loadMoreData:" + itemBeanList.toString());
        }

    }

    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            mTvToast.setText(String.format(getResources().getString(R.string.news_toast), num + ""));
        } else {
            mTvToast.setText("将为你减少此内容");
        }
        mTopToast.setVisibility(View.VISIBLE);
        ViewAnimator.animate(mTopToast)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        ViewAnimator.animate(mTopToast)
                                .bounceOut()
                                .duration(1000)
                                .start();
                    }
                });

    }


    private void bannerToRead(NewsDetail.ItemBean itemBean) {
        if (itemBean == null) {
            return;
        }
        switch (itemBean.getType()) {
            case NewsUtils.TYPE_DOC:
                Intent intent = new Intent(getActivity(), ArticleReadActivity.class);
                intent.putExtra("aid", itemBean.getDocumentId());
                startActivity(intent);
                break;
            case NewsUtils.TYPE_SLIDE:
                ImageBrowseActivity.launch(getActivity(),itemBean);
                break;
            case NewsUtils.TYPE_ADVERT:
                AdvertActivity.launch(getActivity(),itemBean.getLink().getWeburl());
                break;
            case NewsUtils.TYPE_PHVIDEO:
                Toast("TYPE_PHVIDEO");
                break;

        }

    }

    private void toRead(NewsDetail.ItemBean itemBean) {
        if (itemBean == null) {
            return;
        }
        switch (itemBean.getItemType()) {
            case NewsDetail.ItemBean.TYPE_DOC_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG:
                Intent intent = new Intent(getActivity(), ArticleReadActivity.class);
                intent.putExtra("aid", itemBean.getDocumentId());
                startActivity(intent);
                break;
            case NewsDetail.ItemBean.TYPE_SLIDE:
                ImageBrowseActivity.launch(getActivity(),itemBean);
                break;
            case NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG:
            case NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG:
                AdvertActivity.launch(getActivity(),itemBean.getLink().getWeburl());
                break;
            case NewsDetail.ItemBean.TYPE_PHVIDEO:
                Toast("TYPE_PHVIDEO");
                break;
        }
    }
}

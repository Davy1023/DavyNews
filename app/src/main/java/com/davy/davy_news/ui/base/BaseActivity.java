package com.davy.davy_news.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davy.davy_news.R;
import com.davy.davy_news.ui.inter.IBase;
import com.davy.davy_news.widget.MultiStateView;
import com.davy.davy_news.widget.SimpleMultiStateView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

public abstract class BaseActivity<T1 extends BaseContract.BasePresenter> extends SupportActivity implements IBase,BaseContract.BaseView,BGASwipeBackHelper.Delegate{

    protected View mRootView;
    Unbinder unbinder;

    @Nullable
    @BindView(R.id.SimpleMultiStateView)
    SimpleMultiStateView mSimpleMultiStateView;

    @Nullable
    @Inject
    protected T1 mPresenter;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        mRootView = createView(null,null,savedInstanceState);
        setContentView(mRootView);
        attachView();
        bindView(mRootView,savedInstanceState);
        initStateView();
        initData();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(getContentLayout(),container);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public View getView() {
        return mRootView;
    }

    private void attachView() {
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    private void initStateView() {
        if(mSimpleMultiStateView == null) return;
        mSimpleMultiStateView.setEmptyResource(R.layout.view_empty)
                 .setRetryResource(R.layout.view_retry)
                 .setLoadingResource(R.layout.view_loading)
                 .setNoNetResource(R.layout.view_nonet)
                 .build()
                 .setonReLoadlistener(new MultiStateView.onReLoadlistener() {
                     @Override
                     public void onReload() {
                         onRetry();
                     }
                 });
    }
    /**
     * 初始滑动返回
     */
    private void initSwipeBackFinish() {
        // 必须在Application的onCreate方法中执行BGASwipeBackMannager.getInstance().init(this)来初始化滑动返回
        mSwipeBackHelper = new BGASwipeBackHelper(this,this);
        //设置滑动是否可用。默认值为true
        mSwipeBackHelper.setSwipeBackEnable(true);
        //设置是否仅仅跟踪左侧边缘滑动返回。默认值为true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        //设置是否是微信滑动返回方式。默认值为true
        mSwipeBackHelper.setIsWeChatStyle(true);
        //设置阴影资源id
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        //设置是否显示滑动返回的阴影效果。默认值为true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        //设置区域的透明度是否根据滑动的距离渐变。默认值为true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        //设置触发释放后自动返回的阀值，默认为0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);

    }

    /**
     * 是否支持滑动返回。这是在父类中默认返回true支持滑动返回，如果在某个界面不想支持滑动返回重写该方法返回false即可
     * @return
     */

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    protected SimpleMultiStateView getStateView(){
        return mSimpleMultiStateView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        if(mSimpleMultiStateView!=null){
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess() {
        if(mSimpleMultiStateView!=null){
            mSimpleMultiStateView.showContent();
        }
    }

    @Override
    public void showFaild() {
        if(mSimpleMultiStateView!=null){
            mSimpleMultiStateView.showErrorView();
        }
    }

    @Override
    public void showNoNet() {
        if(mSimpleMultiStateView!=null){
            mSimpleMultiStateView.showNoNetView();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }
}

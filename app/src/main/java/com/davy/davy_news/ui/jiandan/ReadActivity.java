package com.davy.davy_news.ui.jiandan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.davy.davy_news.R;
import com.davy.davy_news.bean.FreshNewsArticleBean;
import com.davy.davy_news.bean.FreshNewsBean;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.net.BaseObserver;
import com.davy.davy_news.net.JianDanApi;
import com.davy.davy_news.net.RxSchedulers;
import com.davy.davy_news.ui.base.BaseActivity;
import com.davy.davy_news.utils.DateUtil;
import com.davy.davy_news.utils.ImageLoaderUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: Davy
 * date: 18/8/16
 */
public class ReadActivity extends BaseActivity {

    private static final String DATA = "data";

    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.tv_author)
    TextView mAuthor;
    @BindView(R.id.tv_excerpt)
    TextView mExcerpt;
    @BindView(R.id.linearLayout4)
    LinearLayout mLinearLayout4;
    @BindView(R.id.wv_contnet)
    WebView mWebView;
    @BindView(R.id.progress_wheel)
    ProgressBar mProgress;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.iv_back)
    ImageView mBack;
    @BindView(R.id.iv_share)
    ImageView mShare;
    @BindView(R.id.iv_comment)
    ImageView mComment;

    JianDanApi mJianDanApi;
    FreshNewsBean.PostsBean mPostsBean;
    FreshNewsArticleBean mArticleBean;

    public static void launch(Context context, FreshNewsBean.PostsBean postsBean){
        Intent intent = new Intent(context,ReadActivity.class);
        intent.putExtra(DATA,postsBean);
        context.startActivity(intent);

    }
    @Override
    public int getContentLayout() {
        return R.layout.activity_read;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {
          mJianDanApi = applicationComponent.getJianDanApi();
    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        if(getIntent().getExtras() == null) return;
        mPostsBean = (FreshNewsBean.PostsBean) getIntent().getSerializableExtra(DATA);
        if(mPostsBean == null) return;
        mTitle.setText(mPostsBean.getTitle());
        mAuthor.setText(mPostsBean.getAuthor().getName() + " "
            + DateUtil.getTimestampString(DateUtil.string2Date(mPostsBean.getDate(),"yyyy-MM-dd HH:mm:ss")));
        mExcerpt.setText(mPostsBean.getExcerpt());
        ImageLoaderUtil.LoadImage(this,mPostsBean.getCustom_fields().getThumb_c().get(0),mIvLogo);

        showSuccess();

        setWebViewSetting();

    }

    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {
        getData(mPostsBean.getId());
    }


    private void setWebViewSetting() {
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setVerticalScrollbarOverlay(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setHorizontalScrollbarOverlay(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.loadUrl("file:///android_asset/jd/post_detail.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getData(mPostsBean.getId());
            }
        });
    }

    private void getData(int id) {
        mJianDanApi.getFreshNewsArticle(id)
                .compose(RxSchedulers.<FreshNewsArticleBean>applySchedulers())
                .compose(this.<FreshNewsArticleBean>bindToLife())
                .subscribe(new BaseObserver<FreshNewsArticleBean>() {
                    @Override
                    public void onSuccess(final FreshNewsArticleBean freshNewsArticleBean) {
                            if(freshNewsArticleBean == null){
                                showFaild();
                            }else {
                                mArticleBean = freshNewsArticleBean;
                                mWebView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgress.setVisibility(View.GONE);
                                        final String content = freshNewsArticleBean.getPost().getContent();
                                        String url = "javascript:show_content(\'" + content + "\')";
                                        mWebView.loadUrl(url);
                                    }
                                });
                            }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        showFaild();
                    }
                });
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }

    @OnClick({R.id.iv_back, R.id.iv_share, R.id.iv_comment})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_comment:
                break;
        }
    }
}

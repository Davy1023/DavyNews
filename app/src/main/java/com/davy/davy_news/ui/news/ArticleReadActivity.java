package com.davy.davy_news.ui.news;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.davy.davy_news.R;
import com.davy.davy_news.bean.NewsArticleBean;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.component.DaggerHttpComponent;
import com.davy.davy_news.ui.base.BaseActivity;
import com.davy.davy_news.ui.news.Presenter.ArticleReadPresenter;
import com.davy.davy_news.ui.news.contract.ArticleReadContract;
import com.davy.davy_news.utils.DateUtil;
import com.davy.davy_news.widget.ObservableScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleReadActivity extends BaseActivity<ArticleReadPresenter> implements ArticleReadContract.View {
    static final String TAG = ArticleReadActivity.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_topLogo)
    ImageView mIvTopLogo;
    @BindView(R.id.tv_topname)
    TextView mTvTopname;
    @BindView(R.id.tv_TopUpdateTime)
    TextView mTvTopUpdateTime;
    @BindView(R.id.rl_top)
    RelativeLayout mRlTop;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_updateTime)
    TextView mTvUpdateTime;
    @BindView(R.id.bt_like)
    Button mBtLike;
    @BindView(R.id.ConstraintLayout)
    RelativeLayout mConstraintLayout;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.ScrollView)
    ObservableScrollView mScrollView;

    @Override
    public int getContentLayout() {
        return R.layout.activity_artcleread;
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
        setWebViewSetting();
        mScrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if(y > mConstraintLayout.getHeight()){
                    mRlTop.setVisibility(View.VISIBLE);
                }else {
                    mRlTop.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setWebViewSetting() {
        addjs(mWebview);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebview.setVerticalScrollBarEnabled(false);
        mWebview.setVerticalScrollbarOverlay(false);
        mWebview.setHorizontalScrollBarEnabled(false);
        mWebview.setVerticalScrollbarOverlay(false);
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.loadUrl("file:///android_asset/ifeng/post_detail.html");
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String aid = getIntent().getStringExtra("aid");
                mPresenter.getData(aid);
            }
        });


    }

    private void addjs(final WebView mWebview) {

        class JsObject{
            @JavascriptInterface
            public void jsFunctionimg(final String i){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"run:" + i);
                    }
                });
            }
        }

        mWebview.addJavascriptInterface(new JsObject(),"jscontrolimg");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {
        String aid = getIntent().getStringExtra("aid");
        mPresenter.getData(aid);
    }

    @Override
    public void loadData(final NewsArticleBean articleBean) {
        mTvTitle.setText(articleBean.getBody().getTitle());
        mTvUpdateTime.setText(DateUtil.getTimestampString(DateUtil.string2Date(articleBean.getBody().getUpdateTime(),"yyyy/MM/dd HH:mm:ss")));
        if(articleBean.getBody().getSubscribe()!=null){
            Glide.with(this).load(articleBean.getBody().getSubscribe().getLogo())
                    .apply(new RequestOptions()
                    .transform(new CircleCrop())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    ).into(mIvLogo);
            Glide.with(this).load(articleBean.getBody().getSubscribe().getLogo())
                    .apply(new RequestOptions()
                    .transform(new CircleCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(mIvTopLogo);
            mTvTopname.setText(articleBean.getBody().getSubscribe().getCateSource());
            mTvName.setText(articleBean.getBody().getSubscribe().getCateSource());
            mTvTopUpdateTime.setText(articleBean.getBody().getSubscribe().getCatename());
        }else {
            mTvTopname.setText(articleBean.getBody().getSource());
            mTvName.setText(articleBean.getBody().getSource());
            mTvTopUpdateTime.setText(!TextUtils.isEmpty(articleBean.getBody().getAuthor()) ? articleBean.getBody().getAuthor() : articleBean.getBody().getEditorcode() );
        }
        mWebview.post(new Runnable() {
            @Override
            public void run() {
                final String content = articleBean.getBody().getText();
                String url = "javascript:show_content(\'" + content + "\')";
                mWebview.loadUrl(url);
                showSuccess();
            }
        });
    }

   @OnClick(R.id.iv_back)
    public void onViewClicked(){
        finish();
   }

}

package com.davy.davy_news.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.davy.davy_news.R;
import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: Davy
 * date: 18/8/4
 */
public class AdvertActivity extends BaseActivity {

    private static final String Url = "url";

    @BindView(R.id.pb_progress)
    ProgressBar mPbProgress;
    @BindView(R.id.webview_advert)
    WebView mWebview;

    public static void launch(Context context,String url){
        Intent intent= new Intent(context,AdvertActivity.class);
        intent.putExtra(Url,url);
        context.startActivity(intent);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_advert;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        setWebViewSetting(mWebview);
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        mWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(mPbProgress == null){
                    return;
                }
                if( newProgress == 100){
                    mPbProgress.setVisibility(View.GONE);
                }else {
                    mPbProgress.setVisibility(View.VISIBLE);
                    mPbProgress.setProgress(newProgress);
                }
            }
        });

    }

    private void setWebViewSetting(WebView webview) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setUseWideViewPort(false);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

    }

    @Override
    public void initData() {
        if(getIntent() == null) return;
        String url = getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)){
            mWebview.loadUrl(url);
        }
    }

    @Override
    public void onRetry() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked(){
        finish();
    }

}

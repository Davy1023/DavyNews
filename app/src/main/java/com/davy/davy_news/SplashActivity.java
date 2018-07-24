package com.davy.davy_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davy.davy_news.component.ApplicationComponent;
import com.davy.davy_news.ui.base.BaseActivity;
import com.davy.davy_news.utils.ImageLoaderUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.gifImageView)
    GifImageView gifImageView;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.fl_ad)
    FrameLayout flAd;

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initInjector(ApplicationComponent applicationComponent) {

    }

    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        final GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
        gifDrawable.setLoopCount(1);
        gifImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                gifDrawable.start();
            }
        },100);

        ImageLoaderUtil.LoadImage(this,"http://api.dujin.org/bing/1920.php",ivAd);

        mCompositeDisposable.add(countDown(3).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                tvSkip.setText("跳过 4");
            }
        }).subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                tvSkip.setText("跳过 " + (integer + 1));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                toMainActivity();
            }
        }));

    }

    @Override
    protected void onDestroy() {
        if(mCompositeDisposable != null){
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    private void toMainActivity() {
                if(mCompositeDisposable != null){
                    mCompositeDisposable.dispose();
                }
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

    }

    public Observable<Integer> countDown(int time){
        if(time < 0) time = 0;
        final int countTime = time;
        return Observable.interval(0,1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(@NonNull Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                }).take(countTime + 1);
    }

    @OnClick(R.id.fl_ad)
    public void onViewClicked(){
        toMainActivity();
    }
    @Override
    public void initData() {

    }

    @Override
    public void onRetry() {

    }
}

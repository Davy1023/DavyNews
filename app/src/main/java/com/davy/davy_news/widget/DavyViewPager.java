package com.davy.davy_news.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * author: Davy
 * date: 18/8/5
 */

public class DavyViewPager extends ViewPager {


	private static final String TAG = "HackyViewPager";

	public DavyViewPager(Context context) {
		super(context);
	}

	public DavyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			Log.e(TAG,"hacky viewpager error1");
			return false;
		}catch(ArrayIndexOutOfBoundsException e ){
			Log.e(TAG,"hacky viewpager error2");
			return false;
		}
	}

}

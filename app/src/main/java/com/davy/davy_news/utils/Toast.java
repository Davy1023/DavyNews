package com.davy.davy_news.utils;

import android.content.Context;
import android.view.Gravity;

/**
 * Toast统一管理类
 */
public class Toast {

    public static android.widget.Toast toast = null;

    private Toast() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow)
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * @param title
     * @return返回一个默认的toast
     */
    public static void showDefultToast(Context context, String title) {
        if (toast == null) {
            toast = android.widget.Toast.makeText(context, title, android.widget.Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            toast.setText(title);
            toast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow)
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow)
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow)
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow)
            android.widget.Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow)
            android.widget.Toast.makeText(context, message, duration).show();
    }

}
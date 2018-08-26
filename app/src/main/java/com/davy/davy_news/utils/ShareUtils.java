package com.davy.davy_news.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * author: Davy
 * date: 18/8/25
 */
public class ShareUtils {

    public static void shareText(Context context,String content){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,content);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent,"分享到"));
    }

    public static void shareImage(Context context,String imageUrl){
        Uri imageUri = Uri.parse(imageUrl);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent,"分享到"));
    }
}

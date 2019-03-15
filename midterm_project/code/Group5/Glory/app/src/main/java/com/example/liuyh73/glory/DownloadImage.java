package com.example.liuyh73.glory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ryan.rv_gallery.util.DLog;
import java.util.HashMap;
import java.util.Map;

public class DownloadImage {
    public static void setViewImage(final ImageView imageView, final String url) {
        @SuppressLint("HandlerLeak")
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp=(Bitmap)msg.obj;
                        imageView.setImageBitmap(bmp);
                        break;
                }
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run(){
                Bitmap bmp = HttpUtils.getNetWorkBitmap(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                handle.sendMessage(msg);
            }
        }).start();
    }
    public static void setLinnerLayoutViewImage(final LinearLayout linearLayout, final String url, final Context mRecyclerViewContext){
        @SuppressLint("HandlerLeak")
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp=(Bitmap)msg.obj;
                        bmp = BlurBitmapUtil.cropBitmap(bmp);
                        BlurBitmapUtil.setBlur(bmp,linearLayout,mRecyclerViewContext);
                        break;
                }
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run(){
                Bitmap bmp = HttpUtils.getNetWorkBitmap(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                handle.sendMessage(msg);
            }
        }).start();
    }

    public static void setRelativeLayoutBackground(final RelativeLayout relativeLayout, final String url, final Context ctx){
        @SuppressLint("HandlerLeak")
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp=(Bitmap)msg.obj;
                        Drawable drawable = new BitmapDrawable(ctx.getResources(), bmp);
                        relativeLayout.setBackground(drawable);
                        break;
                }
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run(){
                Bitmap bmp = HttpUtils.getNetWorkBitmap(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                handle.sendMessage(msg);
            }
        }).start();
    }
    public static void setViewImageCut(final ImageView imageView, final String url){
        @SuppressLint("HandlerLeak")
        final Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp=(Bitmap)msg.obj;
                        bmp = BlurBitmapUtil.cropBitmap(bmp);
                        imageView.setImageBitmap(bmp);
                        break;
                }
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run(){
                Bitmap bmp = HttpUtils.getNetWorkBitmap(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                handle.sendMessage(msg);
            }
        }).start();
    }
}
